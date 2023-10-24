package com.fos.api.model;

import com.fos.api.common.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long orderedDate;
    private Long deliveredDate;
    private Constants.OrderStatus orderStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItem> orderItems;
    private double totalAmount;
    @NotNull(message = "User Id is required")
    private Integer userId;
}
