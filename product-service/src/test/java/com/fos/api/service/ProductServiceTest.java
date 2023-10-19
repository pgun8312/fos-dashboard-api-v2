package com.fos.api.service;

import com.fos.api.exception.ProcessFailureException;
import com.fos.api.exception.ValidationFailureException;
import com.fos.api.model.Product;
import com.fos.api.model.request.ProductRequest;
import com.fos.api.model.response.ProductResponse;
import com.fos.api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.config.name=application-test")
public class ProductServiceTest {

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    public void createProduct_shouldThrowValidationFailureException_WhenRequestProductNameExists(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Fresh Chicken");
        productRequest.setDescription("Chicken meat");
        productRequest.setPrice(5.66);

        //mock the behavior
        when(productRepository.existsByName(productRequest.getProductName())).thenReturn(true);

        //test the function throws expected exception
        assertThrows(ValidationFailureException.class, () ->{
            productService.createProduct(productRequest);
        });
    }

    @Test
    public void createProduct_shouldThrowProcessFailureException_WhenRuntimeExceptionThrown(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Fresh Chicken");
        productRequest.setDescription("Chicken meat");
        productRequest.setPrice(5.66);

        //mock the behavior productName doesn't exist
        when(productRepository.existsByName(productRequest.getProductName())).thenReturn(false);

        //mock the productRepository throw an exception
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Unexpected Failure"));

        //testing the expected behaviour
        assertThrows(ProcessFailureException.class, () ->{
            productService.createProduct(productRequest);
        });

    }

    @Test
    public void createProduct_shouldReturnProduct_When_DetailsInserted_Successfully()  {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Fresh Chicken");
        productRequest.setDescription("Chicken meat");
        productRequest.setPrice(5.66);

        //expected product
        Product product = new Product();
        product.setName("Fresh Chicken");
        product.setDescription("Chicken meat");
        product.setPrice(5.66);
        product.setStatus("available");

        when(productRepository.existsByName(productRequest.getProductName())).thenReturn(false);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(productRequest);

        assertEquals(product, createdProduct);
    }

    @Test
    public void getAllProduct_shouldThrowProcessFailureException_WhenRunTimeExceptionThrown (){
        when(productRepository.findAll()).thenThrow(new RuntimeException("Unexpected Failure"));

        assertThrows(ProcessFailureException.class, () -> {
            productService.getAllProduct();
        });
    }

    @Test
    public void getAllProduct_shouldReturnProductList_When_RunTimeExceptionNotThrown () {
        List<Product> productList = List.of(
                getProduct("Chicken", "Fresh Chicken meat", 5.66, "available"),
                getProduct("Carrot", "Fresh Carrot", 1.66, "available")
        );

        //mock the behavior to return list of product
        when(productRepository.findAll()).thenReturn(productList);

        //test service method return list or product responses
        List<Product> products = productService.getAllProduct();

        assertIterableEquals(productList, products);


    }

    @Test
    public void getProductById_shouldThrowValidationFailureException_WhenProductDoesNotExist() {

        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ValidationFailureException.class , () -> {
            productService.getProductById(1);
        });
    }

    @Test
    public void getProductById_shouldReturnProduct_When_ProductExists() {
        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        Product receivedProduct = productService.getProductById(1);

        assertEquals(product, receivedProduct);

    }

    @Test
    public void updateProduct_shouldThrowValidationFailureException_WhenProductDoesNotExist() {
        int productId = 1;
        ProductRequest productRequest = getProductRequest("Chicken", "Fresh Chicken meat", 5.66);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ValidationFailureException.class, () -> {
            productService.updateProduct(productId,productRequest);
        });
    }

    @Test
    public void updateProduct_shouldThrowValidationFailureException_When_ProductIdExists_DuplicateName () {
        int productId = 1;
        ProductRequest productRequest = getProductRequest("Chicken", "Fresh Chicken meat", 5.66);
        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndIdNot(productRequest.getProductName(), productId)).thenReturn(true);

        assertThrows(ValidationFailureException.class, () -> {
            productService.updateProduct(productId,productRequest);
        });
    }
    @Test
    public void updateProduct_shouldThrowProcessFailureException_WhenRuntimeExceptionOccurs () {
        int productId = 1;
        ProductRequest productRequest = getProductRequest("Chicken", "Fresh Chicken meat", 5.66);
        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");

        // Mock the behavior of productRepository.findById
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        // Mock the behavior of productRepository.existsByNameAndIdNot
        when(productRepository.existsByNameAndIdNot(productRequest.getProductName(), productId)).thenReturn(false);
        // Mock the behavior of productRepository.save
        when(productRepository.save(product)).thenThrow(new RuntimeException("Unexpected Exception"));

        assertThrows(ProcessFailureException.class, () -> {
            productService.updateProduct(productId,productRequest);
        });
    }

    @Test
    public void updateProduct_shouldUpdateProduct_WhenValidProductIdAndNoDuplicateName() {
        int productId = 1;
        ProductRequest productRequest = getProductRequest("Updated Chicken", "Updated Fresh Chicken meat", 6.66);
        Product existingProduct = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");

        // Mock the behavior of productRepository.findById
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        // Mock the behavior of productRepository.existsByNameAndIdNot
        when(productRepository.existsByNameAndIdNot(productRequest.getProductName(), productId)).thenReturn(false);


        Product updatedProduct = productService.updateProduct(productId,productRequest);

        // Assert that the product was updated successfully
        assertNotNull(updatedProduct);
        assertEquals(productRequest.getProductName(), updatedProduct.getName());
        assertEquals(productRequest.getDescription(), updatedProduct.getDescription());
        assertEquals(productRequest.getPrice(), updatedProduct.getPrice());
    }

    @Test
    public void deleteProduct_shouldThrowValidationFailureException_WhenProductDoesNotExist() {
        int productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ValidationFailureException.class, () -> {
            productService.deleteProduct(productId);
        });
    }


    @Test
    public void deleteProduct_shouldThrowProcessFailureException_WhenRuntimeExceptionOccurs () {
        int productId = 1;
        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");

        // Mock the behavior of productRepository.findById
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        // Mock the behavior of productRepository.save
        when(productRepository.save(product)).thenThrow(new RuntimeException("Unexpected Exception"));

        assertThrows(ProcessFailureException.class, () -> {
            productService.deleteProduct(productId);
        });
    }

    public void deleteProduct_shouldDeleteProduct_When_ValidProductIdProvided() {
        int productId = 1;
        Product existingProduct = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");

        // Mock the behavior of productRepository.findById
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        Product deletedProduct = productService.deleteProduct(productId);

        // Assert that the product was Deleted successfully(set availability to noAvailable)
        assertNotNull(deletedProduct);
        assertEquals(existingProduct.getName(), deletedProduct.getName());
        assertEquals(existingProduct.getDescription(), deletedProduct.getDescription());
        assertEquals(existingProduct.getPrice(), deletedProduct.getPrice());
        assertNotEquals(existingProduct.getStatus(), deletedProduct.getStatus());
    }

    private ProductRequest getProductRequest(String productName, String description, Double price) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName(productName);
        productRequest.setDescription(description);
        productRequest.setPrice(price);

        return productRequest;

    }

    private Product getProduct(String productName, String description, Double price, String status) {
        Product product = new Product();
        product.setName(productName);
        product.setDescription(description);
        product.setPrice(price);
        product.setStatus(status);

        return product;

    }

}
