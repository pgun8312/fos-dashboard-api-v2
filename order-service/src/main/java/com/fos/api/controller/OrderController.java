package com.fos.api.controller;

import com.fos.api.exception.ErrorInfo;
import com.fos.api.exception.ProcessFailureException;
import com.fos.api.exception.ValidationFailureException;
import com.fos.api.model.Order;
import com.fos.api.model.request.OrderUpdateRequest;
import com.fos.api.model.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fos.api.model.request.OrderRequest;
import com.fos.api.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Endpoints for managing orders")
public class OrderController {

    final private OrderService orderService;
    @PostMapping("/orders")
    @Operation(
            summary = "Place an Order",
            description = "This endpoint allows you to place a new order"
    )
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest);

        OrderResponse response = orderToOrderResponse(createdOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/orders")
    @Operation(
            summary = "Get all orders",
            description = "Retrieve a list of orders"
    )
    public ResponseEntity<List<OrderResponse>> getAllOrders(){
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponse> orderResponses = orders.stream().map(this::orderToOrderResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(orderResponses);
    }

    @GetMapping("/orders/{orderId}")
    @Operation(
            summary = "Get Order by ID",
            description = "Retrieve a specific order by its unique identifier"
    )
    public ResponseEntity<OrderResponse> getOrderById( @NotNull @PathVariable("orderId") Integer orderId) {
        Order order = orderService.getOrderById(orderId);

        OrderResponse orderResponse = orderToOrderResponse(order);

        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @PatchMapping("/orders/{orderId}")
    @Operation(
            summary = "Update order by ID",
            description = "Update a specific order by ID"
    )
    public ResponseEntity<OrderResponse> updateOrder(@NotNull @PathVariable("orderId") Integer orderId,@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        Order updatedOrder = orderService.updateOrder(orderId, orderUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(orderToOrderResponse(updatedOrder));
    }

    @DeleteMapping("/orders/{orderId}")
    @Operation(
            summary = "Cancel Order",
            description = "Cancel a specific order by ID"
    )
    public ResponseEntity<OrderResponse> cancelOrder(@NotNull @PathVariable("orderId") Integer orderId){
        Order canceledOrder = orderService.cancelOrder(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(orderToOrderResponse(canceledOrder));
    }

    @GetMapping("/users/{userId}/orders")
    @Operation(
            summary = "Get orders by User Id",
            description = "Retrieve orders associated with a user by their userId"
    )
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@NotNull @PathVariable("userId") Integer userId) {
        List<Order> orderList = orderService.getOrdersByUserId(userId);

        List<OrderResponse> orderResponses = orderList.stream().map(this::orderToOrderResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(orderResponses);
    }



    private OrderResponse orderToOrderResponse(Order createdOrder) {
        try {
            OrderResponse response = new OrderResponse();
            response.setId(createdOrder.getId());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String orderedDate = dateFormat.format(new Date(createdOrder.getOrderedDate()));
            response.setOrderedDate(orderedDate);
            response.setOrderStatus(createdOrder.getOrderStatus());
            response.setOrderItems(createdOrder.getOrderItems());
            response.setTotalAmount(createdOrder.getTotalAmount());
            response.setUserId(createdOrder.getUserId());

            // Check if the deliveredDate is not null in the createdOrder and set it in the response
            if (createdOrder.getDeliveredDate() != null) {
                String deliveredDate = dateFormat.format(new Date(createdOrder.getDeliveredDate()));
                response.setDeliveredDate(deliveredDate);
            }
            return response;
        }
        catch(IllegalArgumentException e){
            throw new ValidationFailureException(ErrorInfo.INVALID_DATE_FORMAT);
        }
        catch(Exception e) {
            throw new ProcessFailureException(ErrorInfo.PROCESS_FAILURE, "Cannot Create the Product Response");
        }
    }
}
