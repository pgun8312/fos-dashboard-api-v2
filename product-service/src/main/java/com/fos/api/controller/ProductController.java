package com.fos.api.controller;

import com.fos.api.model.Product;
import com.fos.api.model.request.ProductRequest;
import com.fos.api.model.response.ProductResponse;
import com.fos.api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = productService.createProduct(request);

        ProductResponse response = convertToResponse(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct() {

        List<Product> products = productService.getAllProduct();

        List<ProductResponse> response  = products.stream().map(this::convertToResponse).toList();

        if (!response.isEmpty()) {

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Integer productId){
        Product product = productService.getProductById(productId);

        //checking wether prodcut is exist or not
        if(product != null ) {
            ProductResponse response = convertToResponse(product);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("productId") Integer productId,@Valid @RequestBody ProductRequest request) {
        Product product = productService.updateProduct(productId,request);
        if(product != null) {
           ProductResponse response = convertToResponse(product);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{productId}")
    public  ResponseEntity<ProductResponse> deleteProduct(@PathVariable("productId") Integer productId){
        Product product = productService.deleteProduct(productId);

        if(product != null) {
            ProductResponse response = convertToResponse(product);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.notFound().build();
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStatus(product.getStatus());
        return response;
    }

}
