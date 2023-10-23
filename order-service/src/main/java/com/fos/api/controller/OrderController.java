package com.fos.api.controller;

import com.fos.api.model.Order;
import com.fos.api.model.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fos.api.model.request.OrderRequest;
import com.fos.api.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    final private OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest);

        OrderResponse response = orderToOrderResponse(createdOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private OrderResponse orderToOrderResponse(Order createdOrder) {
        OrderResponse response = new OrderResponse();
        response.setId(createdOrder.getId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orderedDate = dateFormat.format(new Date(createdOrder.getOrderDate()));
        response.setOrderDate(orderedDate);
        response.setOrderStatus(createdOrder.getOrderStatus());
        response.setOrderItems(createdOrder.getOrderItems());
        response.setTotalAmount(createdOrder.getTotalAmount());
        return response;
    }
}
