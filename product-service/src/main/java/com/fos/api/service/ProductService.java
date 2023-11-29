package com.fos.api.service;

import com.fos.api.common.Constants;
import com.fos.api.controller.ProductController;
import com.fos.api.exception.ErrorInfo;
import com.fos.api.exception.ProcessFailureException;
import com.fos.api.exception.ValidationFailureException;
import com.fos.api.model.Product;
import com.fos.api.model.request.ProductRequest;
import com.fos.api.model.request.ProductUpdateRequest;
import com.fos.api.model.response.ProductResponse;
import com.fos.api.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;

    }

    public Product createProduct(ProductRequest request){
        if(productRepository.existsByName(request.getProductName())) {
            log.warn("Product creation failed due to duplicate product name: {}", request.getProductName());
            throw new ValidationFailureException(ErrorInfo.DUPLICATE_PRODUCT_NAME);
        }

        try{
            // Convert the string category to enum using getCategoryEnum method
            Constants.ProductCategory productCategory = request.getCategoryEnum();

            Product product = new Product(
                request.getProductName(),
                request.getDescription(),
                request.getPrice(),
                "available",
                request.getImage(),
                productCategory,
                request.getRemainingQuantity()
            );

            log.info("Product created with name: {}", product.getName());
            productRepository.save(product);
            return product;
        }
        catch (Exception e){
            log.error("Product creation failed: {}", e.getMessage());
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot create the product");
        }
    }

    public List<Product> getAllProduct(){
        try {
            List<Product> products = productRepository.findAll();
            log.info("Retrieved {} products from the database", products.size());
            return products;
        }
        catch (Exception e) {
            log.error("Failed to get the product list: {}", e.getMessage());
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot get the product list");
        }
    }



    public Product getProductById(Integer productId){
        Optional<Product> existingProduct = productRepository.findById(productId);

        if (existingProduct.isPresent()) {
            log.info("Retrieved product with ID: {}", productId);
            return existingProduct.get();
        }
        log.warn("Failed to retrieve product with ID: {}", productId);
        throw new ValidationFailureException(ErrorInfo.INVALID_PRODUCT_ID);

    }

    public Product updateProduct(Integer productId, ProductUpdateRequest request) {

        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()) {
            log.warn("Failed to update product with ID {} because it doesn't exist", productId);
            throw new ValidationFailureException(ErrorInfo.INVALID_PRODUCT_ID);
        }
        else if(productRepository.existsByNameAndIdNot(request.getProductName(), productId)) {
            log.warn("Failed to update product with ID {} due to duplicate product name: {}", productId, request.getProductName());
            throw new ValidationFailureException(ErrorInfo.DUPLICATE_PRODUCT_NAME);
        }
        else {
            try {

                Constants.ProductCategory productCategory = request.getCategoryEnum();

                Product newProduct = product.get();
                newProduct.setName(request.getProductName());
                newProduct.setDescription(request.getDescription());
                newProduct.setPrice(request.getPrice());
                newProduct.setStatus(request.getStatus());
                newProduct.setImage(request.getImage());
                newProduct.setCategory(productCategory);
                newProduct.setRemainingQuantity(request.getRemainingQuantity());
                productRepository.save(newProduct);
                log.info("Updated product with ID: {}", productId);
                return newProduct;
            } catch (Exception ex) {
                log.error("Product update failed for ID: {} , message: {}", productId, ex.getMessage());
                throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE);
            }
        }

    }

    public Product deleteProduct(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);

        if(!product.isPresent()) {
            log.warn("Failed to delete product with ID {} because it doesn't exist", productId);
            throw new ValidationFailureException(ErrorInfo.INVALID_PRODUCT_ID);
        }
        else{
            try {
                Product deletedproduct = product.get();
                deletedproduct.setStatus("notAvailable");
                productRepository.save(deletedproduct);
                log.info("Deleted product with ID: {}", productId);
                return deletedproduct;
            }
            catch (Exception ex) {
                log.error("Product deletion failed for ID: {}, message: {}", productId, ex.getMessage());
                throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE);
            }
        }
    }
}
