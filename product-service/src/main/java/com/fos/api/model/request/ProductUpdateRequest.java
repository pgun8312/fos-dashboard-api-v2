package com.fos.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

}
