package com.fos.api.repository;

import com.fos.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order, Integer> {

    List<Order> findByUserId(Integer userId);
}
