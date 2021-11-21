package com.anderfred.assignmenttask.repository;

import com.anderfred.assignmenttask.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
