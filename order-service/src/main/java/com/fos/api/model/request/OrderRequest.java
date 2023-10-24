package com.fos.api.model.request;

import com.fos.api.common.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fos.api.model.OrderItem;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private Constants.OrderStatus orderStatus;
    @NotNull
    @Size(min = 1, message = "At Least one item should be present")
    private List<OrderItemRequest> orderItems;
    @NotNull
    private double totalAmount;
    @NotNull(message = "User Id is required")
    private Integer userId;
}
