package com.anderfred.assignmenttask.service;

import com.anderfred.assignmenttask.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemService {
    List<OrderItem> getAll();
    Optional<OrderItem> findById(long id);
    OrderItem save(OrderItem orderItem);
    void deleteAll();
    void deleteById(long id);
}
