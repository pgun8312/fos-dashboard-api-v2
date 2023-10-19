package com.fos.api.service;

import com.fos.api.controller.ProductController;
import com.fos.api.exception.ErrorInfo;
import com.fos.api.exception.ProcessFailureException;
import com.fos.api.exception.ValidationFailureException;
import com.fos.api.model.Product;
import com.fos.api.model.request.ProductRequest;
import com.fos.api.model.response.ProductResponse;
import com.fos.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;

    }

    public Product createProduct(ProductRequest request){
        if(productRepository.existsByName(request.getProductName())) {
            throw new ValidationFailureException(ErrorInfo.DUPLICATE_PRODUCT_NAME);
        }

        try{
            Product product = new Product(
                request.getProductName(),
                request.getDescription(),
                request.getPrice(),
                "available"
            );

            productRepository.save(product);
            return product;
        }
        catch (Exception e){
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot create the product");
        }
    }

    public List<Product> getAllProduct(){
        try {
            return productRepository.findAll();
        }
        catch (Exception e) {
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot get the product list");
        }
    }



    public Product getProductById(Integer productId){
        Optional<Product> existingProduct = productRepository.findById(productId);

        if (existingProduct.isPresent()) {
            return existingProduct.get();
        }

        throw new ValidationFailureException(ErrorInfo.INVALID_PRODUCT_ID);

    }

    public Product updateProduct(Integer productId, ProductRequest request) {

        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()) {
            throw new ValidationFailureException(ErrorInfo.INVALID_PRODUCT_ID);
        }
        else if(productRepository.existsByNameAndIdNot(request.getProductName(), productId)) {
            throw new ValidationFailureException(ErrorInfo.DUPLICATE_PRODUCT_NAME);
        }
        else {
            try {
                Product newProduct = product.get();
                newProduct.setName(request.getProductName());
                newProduct.setDescription(request.getDescription());
                newProduct.setPrice(request.getPrice());
                productRepository.save(newProduct);
                return newProduct;
            } catch (Exception ex) {
                throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE);
            }
        }

    }

    public Product deleteProduct(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);

        if(!product.isPresent()) {
            throw new ValidationFailureException(ErrorInfo.INVALID_PRODUCT_ID);
        }
        else{
            try {
                Product deletedproduct = product.get();
                deletedproduct.setStatus("notAvailable");
                productRepository.save(deletedproduct);

                return deletedproduct;
            }
            catch (Exception ex) {
                throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE);
            }
        }
    }
}
