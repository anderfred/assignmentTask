package com.anderfred.assignmenttask.repository;

import com.anderfred.assignmenttask.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
