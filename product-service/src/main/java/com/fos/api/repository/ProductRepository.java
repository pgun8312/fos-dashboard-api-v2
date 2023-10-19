package com.fos.api.repository;

import com.fos.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String productName);
    boolean existsByNameAndIdNot(String productName, int id);
}
