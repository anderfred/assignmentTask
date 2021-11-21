package com.anderfred.assignmenttask.service;

import com.anderfred.assignmenttask.model.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderService {
    List<Order> getAll();
    Optional<Order> findById(long id);
    Order save(Order order);
    void deleteAll();
    void deleteById(long id);
    String findReportData();
    Set<Order> findByProductNameCustom(String name, Pageable pageable);
}
