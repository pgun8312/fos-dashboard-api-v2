package com.fos.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    @NotBlank(message = "ProductId is Required")
    private Integer productId;
    @NotBlank(message = "Product name is Required")
    private String productName;
    @NotBlank(message = "Unit Price is Required")
    private Double unitPrice;
    @NotBlank(message = "Quantity is Required")
    private Double quantity;
}
