package com.fos.api.service;

import com.fos.api.common.Constants;
import com.fos.api.exception.ErrorInfo;
import com.fos.api.exception.ProcessFailureException;
import com.fos.api.exception.ValidationFailureException;
import com.fos.api.model.Product;
import com.fos.api.model.request.ProductRequest;
import com.fos.api.model.request.ProductUpdateRequest;
import com.fos.api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// TO GENERATE TEST REPORT RUN : mvn clean install jacoco:report
// TO RUN SONAR ANALYSIS RUN : sonar-scanner

@ExtendWith(MockitoExtension.class)// should extend from the mockiteclass
class ProductServiceTest {

    @Mock
    ProductRepository productRepository; //should create mockito Mock bean

    @InjectMocks
    ProductService productService; //need to create inject mock , also the product service repository should autowired

    @Test
    void createProduct_shouldThrowValidationFailureException_WhenRequestProductNameExists(){

        /* ARRANGE */
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Fresh Chicken");

        /* ACT */
        //mock the behavior(using mockito)
        when(productRepository.existsByName(Mockito.any())).thenReturn(true);

        /* ASSERT */
        //test the function throws expected exception
        ValidationFailureException validationFailureException =assertThrows(ValidationFailureException.class, () ->{
            productService.createProduct(productRequest);
        });

        assertEquals(ErrorInfo.DUPLICATE_PRODUCT_NAME.getMessage(), validationFailureException.getMessage());


        verify(productRepository).existsByName(Mockito.any());
    }

    @Test
    void createProduct_shouldThrowProcessFailureException_WhenRuntimeExceptionThrown(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Fresh Chicken");
        productRequest.setDescription("Chicken meat");
        productRequest.setPrice(5.66);
        productRequest.setCategory("SOFT_DRINKS");
        productRequest.setImage("image_1");
        productRequest.setRemainingQuantity(50);

        //mock the behavior productName doesn't exist
        when(productRepository.existsByName(Mockito.any())).thenReturn(false);

        //mock the productRepository throw an exception
        when(productRepository.save(Mockito.any())).thenThrow(new ProcessFailureException(ErrorInfo.PROCESS_FAILURE));

        //testing the expected behaviour
        ProcessFailureException processFailureException = assertThrows(ProcessFailureException.class, () ->{
            productService.createProduct(productRequest);
        });

        assertEquals(ErrorInfo.PROCESS_FAILURE.getMessage(), processFailureException.getMessage());

        verify(productRepository).existsByName(Mockito.any());
//        verify(productRepository).save(Mockito.any());

    }

    @Test
    void createProduct_shouldReturnProduct_When_DetailsInserted_Successfully()  {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Fresh Chicken");
        productRequest.setDescription("Chicken meat");
        productRequest.setPrice(5.66);
        productRequest.setCategory("SOFT_DRINKS");
        productRequest.setImage("image_1");
        productRequest.setRemainingQuantity(50);

        //expected product
        Product product = new Product();
        product.setName("Fresh Chicken");
        product.setDescription("Chicken meat");
        product.setPrice(5.66);
        product.setStatus("available");
        product.setCategory(productRequest.getCategoryEnum());
        product.setImage("image_1");
        product.setRemainingQuantity(50);

        when(productRepository.existsByName(Mockito.any())).thenReturn(false);
        when(productRepository.save(Mockito.any())).thenReturn(product);

        Product createdProduct = productService.createProduct(productRequest);
        assertEquals(product.getName(), createdProduct.getName()); //name should be unique

        verify(productRepository).existsByName(Mockito.any());
        verify(productRepository).save(Mockito.any());
    }

    @Test
    void getAllProduct_shouldThrowProcessFailureException_WhenRunTimeExceptionThrown (){
        // ARRANGE
        when(productRepository.findAll()).thenThrow(new RuntimeException("Unexpected Failure"));

        // ACT
        ProcessFailureException processFailureException = assertThrows(ProcessFailureException.class, () -> {
            productService.getAllProduct();
        });

        // ASSERT
        assertEquals(ErrorInfo.PROCESS_FAILURE.getMessage(), processFailureException.getMessage());
        verify(productRepository).findAll();
    }

    @Test
    void getAllProduct_shouldReturnProductList_When_RunTimeExceptionNotThrown () {
        List<Product> productList = List.of(
                getProduct("Chicken", "Fresh Chicken meat", 5.66, "available"),
                getProduct("Carrot", "Fresh Carrot", 1.66, "available")
        );

        //mock the behavior to return list of product
        when(productRepository.findAll()).thenReturn(productList);

        //test service method return list or product responses
        List<Product> products = productService.getAllProduct();

        assertIterableEquals(productList, products);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_shouldThrowValidationFailureException_WhenProductDoesNotExist() {

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        ValidationFailureException validationFailureException = assertThrows(ValidationFailureException.class , () -> {
            productService.getProductById(1);
        });

        assertEquals(ErrorInfo.INVALID_PRODUCT_ID.getMessage(), validationFailureException.getMessage());
        verify(productRepository).findById(Mockito.any());
    }

    @Test
    void getProductById_shouldReturnProduct_When_ProductExists() {
        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

        Product receivedProduct = productService.getProductById(Mockito.any());

        assertEquals(product, receivedProduct);
        verify(productRepository).findById(Mockito.any());

    }

    @Test
    void updateProduct_shouldThrowValidationFailureException_WhenProductDoesNotExist() {
        Integer productId = 1;
        ProductUpdateRequest productUpdateRequest = getProductUpdateRequest("Chicken", "Fresh Chicken meat", 5.66,"available");

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        ValidationFailureException validationFailureException =assertThrows(ValidationFailureException.class, () -> {
            productService.updateProduct(productId,productUpdateRequest);
        });

        assertEquals(ErrorInfo.INVALID_PRODUCT_ID.getMessage(), validationFailureException.getMessage());
        verify(productRepository).findById(Mockito.any());

    }

    @Test
    void updateProduct_shouldThrowValidationFailureException_When_ProductIdExists_DuplicateName () {
        int productId = 1;
        ProductUpdateRequest productUpdateRequest = getProductUpdateRequest("Chicken", "Fresh Chicken meat", 5.66,"available");
        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndIdNot(Mockito.any(), Mockito.anyInt())).thenReturn(true);

        ValidationFailureException validationFailureException =assertThrows(ValidationFailureException.class, () -> {
            productService.updateProduct(productId,productUpdateRequest);
        });

        assertEquals(ErrorInfo.DUPLICATE_PRODUCT_NAME.getMessage(), validationFailureException.getMessage() );

        verify(productRepository).findById(Mockito.any());
        verify(productRepository).existsByNameAndIdNot(Mockito.any(), Mockito.anyInt());
    }
    @Test
    void updateProduct_shouldThrowProcessFailureException_WhenRuntimeExceptionOccurs () {
        int productId = 1;
        ProductUpdateRequest productUpdateRequest = getProductUpdateRequest("Chicken", "Fresh Chicken meat", 5.66,"available");
        productUpdateRequest.setCategory("SOFT_DRINKS");
        productUpdateRequest.setImage("image_1");
        productUpdateRequest.setRemainingQuantity(50);

        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");
        product.setCategory(productUpdateRequest.getCategoryEnum());
        product.setImage("image_1");
        product.setRemainingQuantity(50);

        // Mock the behavior of productRepository.findById
        when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));
        // Mock the behavior of productRepository.existsByNameAndIdNot
        when(productRepository.existsByNameAndIdNot(Mockito.any(), Mockito.anyInt())).thenReturn(false);
        // Mock the behavior of productRepository.save
        when(productRepository.save(Mockito.any())).thenThrow(new RuntimeException("Unexpected Exception"));

        ProcessFailureException processFailureException = assertThrows(ProcessFailureException.class, () -> {
            productService.updateProduct(productId,productUpdateRequest);
        });

        assertEquals(ErrorInfo.PROCESS_FAILURE.getMessage(), processFailureException.getMessage());

        verify(productRepository).findById(Mockito.anyInt());
        verify(productRepository).existsByNameAndIdNot(Mockito.any(), Mockito.anyInt());
        verify(productRepository).save(Mockito.any());
    }

    @Test
    void updateProduct_shouldUpdateProduct_WhenValidProductIdAndNoDuplicateName() {
        int productId = 1;
        ProductUpdateRequest productUpdateRequest = getProductUpdateRequest("Chicken", "Fresh Chicken meat", 5.66,"available");
        productUpdateRequest.setCategory("SOFT_DRINKS");
        productUpdateRequest.setImage("image_1");
        productUpdateRequest.setRemainingQuantity(50);

        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");
        product.setCategory(productUpdateRequest.getCategoryEnum());
        product.setImage("image_1");
        product.setRemainingQuantity(50);

        // Mock the behavior of productRepository.findById
        when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));
        // Mock the behavior of productRepository.existsByNameAndIdNot
        when(productRepository.existsByNameAndIdNot(Mockito.any(), Mockito.anyInt())).thenReturn(false);
        // Mock the behavior of productRepository.save
        when(productRepository.save(Mockito.any())).thenReturn(product);

        Product updatedProduct = productService.updateProduct(productId,productUpdateRequest);

        // Assert that the product was updated successfully
        assertNotNull(updatedProduct);
        assertEquals(productUpdateRequest.getProductName(), updatedProduct.getName());
        assertEquals(productUpdateRequest.getDescription(), updatedProduct.getDescription());
        assertEquals(productUpdateRequest.getPrice(), updatedProduct.getPrice());
        assertEquals(productUpdateRequest.getStatus(), updatedProduct.getStatus());

        verify(productRepository).findById(Mockito.anyInt());
        verify(productRepository).existsByNameAndIdNot(Mockito.any(), Mockito.anyInt());
        verify(productRepository).save(Mockito.any());
    }

    @Test
    void deleteProduct_shouldThrowValidationFailureException_WhenProductDoesNotExist() {
        int productId = 1;

        when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        ValidationFailureException validationFailureException = assertThrows(ValidationFailureException.class, () -> {
            productService.deleteProduct(productId);
        });

        assertEquals(ErrorInfo.INVALID_PRODUCT_ID.getMessage(), validationFailureException.getMessage());

        verify(productRepository).findById(Mockito.anyInt());
    }


    @Test
    void deleteProduct_shouldThrowProcessFailureException_WhenRuntimeExceptionOccurs () {
        int productId = 1;
        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");
        product.setCategory(Constants.ProductCategory.CHICKEN);
        product.setImage("image_1");
        product.setRemainingQuantity(50);


        // Mock the behavior of productRepository.findById
        when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));
        // Mock the behavior of productRepository.save
        when(productRepository.save(product)).thenThrow(new RuntimeException("Unexpected Exception"));

        ProcessFailureException processFailureException = assertThrows(ProcessFailureException.class, () -> {
            productService.deleteProduct(productId);
        });

        assertEquals(ErrorInfo.PROCESS_FAILURE.getMessage(), processFailureException.getMessage());
        verify(productRepository).findById(Mockito.anyInt());
        verify(productRepository).save(Mockito.any());
    }

    @Test
    void deleteProduct_shouldDeleteProduct_When_ValidProductIdProvided() {
        int productId = 1;
        Product existingProduct = getProduct("Chicken", "Fresh Chicken meat", 5.66, "available");
        existingProduct.setCategory(Constants.ProductCategory.CHICKEN);
        existingProduct.setImage("image_1");
        existingProduct.setRemainingQuantity(50);

        Product product = getProduct("Chicken", "Fresh Chicken meat", 5.66, "notavailable");
        product.setCategory(Constants.ProductCategory.CHICKEN);
        product.setImage("image_1");
        product.setRemainingQuantity(50);

        // Mock the behavior of productRepository.findById
        when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(Mockito.any())).thenReturn(product);

        Product deletedProduct = productService.deleteProduct(productId);

        // Assert that the product was Deleted successfully(set availability to noAvailable)
        assertNotNull(deletedProduct);
        assertEquals(existingProduct.getName(), deletedProduct.getName());
        assertEquals(existingProduct.getDescription(), deletedProduct.getDescription());
        assertEquals(existingProduct.getPrice(), deletedProduct.getPrice());
        assertNotEquals("notavailable", deletedProduct.getStatus());

        verify(productRepository).findById(Mockito.anyInt());
        verify(productRepository).save(Mockito.any());
    }

    private ProductRequest getProductRequest(String productName, String description, Double price) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName(productName);
        productRequest.setDescription(description);
        productRequest.setPrice(price);

        return productRequest;

    }
    private ProductUpdateRequest getProductUpdateRequest(String productName, String description, Double price, String status) {
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setProductName(productName);
        productUpdateRequest.setDescription(description);
        productUpdateRequest.setPrice(price);
        productUpdateRequest.setStatus(status);
        return productUpdateRequest;

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
