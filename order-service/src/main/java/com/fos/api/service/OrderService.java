package com.fos.api.service;

import com.fos.api.exception.ErrorInfo;
import com.fos.api.exception.ProcessFailureException;
import com.fos.api.model.Order;
import com.fos.api.model.OrderItem;
import com.fos.api.model.request.OrderRequest;
import com.fos.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    final private OrderRepository orderRepository;


    public Order createOrder(OrderRequest orderRequest) {
        try{
            Order order = new Order();
            order.setOrderStatus(orderRequest.getOrderStatus());
            order.setOrderDate(System.currentTimeMillis());
            List<OrderItem> orderItems = createOrderItems(orderRequest);

            //set corresponding order to order-items
            for(OrderItem orderItem : orderItems) {
                orderItem.setOrder(order);
            }
            order.setOrderItems(orderItems);
            order.setTotalAmount(orderRequest.getTotalAmount());

            orderRepository.save(order);

            return order;
        }
        catch (Exception e) {
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot placed the order");
        }
    }

    private List<OrderItem> createOrderItems(OrderRequest orderRequest) {
        List<OrderItem> orderItems = new ArrayList<>();
        orderRequest.getOrderItems().forEach((requestOrderItem) -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(requestOrderItem.getProductId());
            orderItem.setUnitPrice(requestOrderItem.getUnitPrice());
            orderItem.setQuantity(requestOrderItem.getQuantity());


            orderItems.add(orderItem);
        });

        return orderItems;
    }
}
