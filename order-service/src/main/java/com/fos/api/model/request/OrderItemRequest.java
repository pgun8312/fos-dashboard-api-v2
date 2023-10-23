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
    @NotBlank(message = "productId is Required")
    private Integer productId;
    @NotBlank(message = "Unit Price is Required")
    private Double unitPrice;
    @NotBlank(message = "Quantity is Required")
    private Double quantity;
}
