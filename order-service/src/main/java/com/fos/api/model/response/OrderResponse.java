package com.fos.api.model.response;

import com.fos.api.common.Constants;
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
    private String orderedDate;
    private String deliveredDate;
    private Constants.OrderStatus orderStatus;
    private List<OrderItem> orderItems;
    private double totalAmount;
    private Integer userId;
}
