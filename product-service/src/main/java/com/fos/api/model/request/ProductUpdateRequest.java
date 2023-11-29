package com.fos.api.model.request;

import com.fos.api.common.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    @NotBlank(message = "Product name is required")
    String productName;
    @NotBlank(message = "Product description is required")
    String description;
    @NotNull(message = "Product price is required")
    @Positive(message = "Product price should be a positive")
    Double price;
    @NotNull(message = "Product status is required")
    String status;
    @NotBlank(message = "Product image is required")
    String image;
    @NotBlank(message = "Product category is required")
    String category; // Change the type to String
    @NotNull(message = "Remaining quantity is required")
    @PositiveOrZero(message = "Remaining quantity should be a positive or zero")
    Integer remainingQuantity;

    public Constants.ProductCategory getCategoryEnum() {
        try {
            return Constants.ProductCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product category");
        }
    }

}
