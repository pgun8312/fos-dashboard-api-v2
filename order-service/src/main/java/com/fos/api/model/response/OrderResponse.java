package com.fos.api.model.response;

import lombok.*;
import com.fos.api.model.OrderItem;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Integer id;
    private String orderDate;
    private String orderStatus;
    private List<OrderItem> orderItems;
    private double totalAmount;
}
