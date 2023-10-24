package com.fos.api.service;

import com.fos.api.common.Constants;
import com.fos.api.exception.ErrorInfo;
import com.fos.api.exception.ProcessFailureException;
import com.fos.api.exception.ValidationFailureException;
import com.fos.api.model.Order;
import com.fos.api.model.OrderItem;
import com.fos.api.model.request.OrderRequest;
import com.fos.api.model.request.OrderUpdateRequest;
import com.fos.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    final private OrderRepository orderRepository;


    public Order createOrder(OrderRequest orderRequest) {
        try{
            Order order = new Order();
            order.setOrderStatus(orderRequest.getOrderStatus());
            order.setOrderedDate(System.currentTimeMillis());
            List<OrderItem> orderItems = createOrderItems(orderRequest);
            order.setUserId(orderRequest.getUserId());

            //set corresponding order to order-items
            for(OrderItem orderItem : orderItems) {
                orderItem.setOrder(order);
            }
            order.setOrderItems(orderItems);
            order.setTotalAmount(orderRequest.getTotalAmount());
            log.info("Order created with ID: {}", order.getId());
            orderRepository.save(order);

            return order;
        }
        catch (Exception e) {
            log.error("Failed to create an order: {}", e.getMessage());
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot placed the order");
        }
    }

    public List<Order> getAllOrders() {
        try {
            log.info("Retrieving all orders");
            return orderRepository.findAll();
        }
        catch(Exception e) {
            log.error("Failed to get the orders: {}", e.getMessage());
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot get the Orders");
        }

    }

    //to convert request order item to order items
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


    public Order getOrderById(Integer orderId) {

        Optional<Order> existingOrder = orderRepository.findById(orderId);

        if (existingOrder.isPresent()) {
            log.info("Retrieving order with ID: {}", orderId);
           return existingOrder.get();
        }
        log.warn("Failed to retrieve order with ID: {}", orderId);
        throw new ValidationFailureException(ErrorInfo.INVALID_ORDER_ID);
    }

    public Order updateOrder(Integer orderId, OrderUpdateRequest orderUpdateRequest) {

        Optional<Order> existingOrder = orderRepository.findById(orderId);

        if(!existingOrder.isPresent()) {
            throw new ValidationFailureException(ErrorInfo.INVALID_ORDER_ID);
        }

        try{
            Order updatedOrder = existingOrder.get();
            updatedOrder.setDeliveredDate(System.currentTimeMillis());
            updatedOrder.setOrderStatus(orderUpdateRequest.getStatus());
            log.info("Order updated with ID: {}", orderId);
            orderRepository.save(updatedOrder);

            return updatedOrder;
        }
        catch(Exception e) {
            log.error("Failed to update order with ID: {}, {}", orderId, e.getMessage());
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot update the order");
        }

    }

    public Order cancelOrder(Integer orderId) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);

        if(existingOrder.isPresent()) {
            try {
                Order canceledOrder = existingOrder.get();
                canceledOrder.setOrderStatus(Constants.OrderStatus.CANCELED);
                log.info("Canceled the order with ID: {}", orderId);
                return orderRepository.save(canceledOrder);
            }
            catch(Exception e) {
                log.error("Failed to cancel order with ID: {}, {}", orderId, e.getMessage());
                throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot cancel the order");
            }
        }

        throw new ValidationFailureException(ErrorInfo.INVALID_ORDER_ID);
    }

    public List<Order> getOrdersByUserId(Integer userId) {
     try{
         log.info("Retrieving orders for user ID: {}", userId);
         return orderRepository.findByUserId(userId);
     }
     catch(Exception e) {
         log.error("Failed to retrieve orders for user ID: {}", userId, e);
         throw new ValidationFailureException(ErrorInfo.INVALID_USER_ID);
     }
    }
}
