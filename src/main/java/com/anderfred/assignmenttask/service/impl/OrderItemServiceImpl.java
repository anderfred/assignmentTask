package com.anderfred.assignmenttask.service.impl;

import com.anderfred.assignmenttask.model.OrderItem;
import com.anderfred.assignmenttask.repository.OrderItemRepository;
import com.anderfred.assignmenttask.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    OrderItemRepository repository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<OrderItem> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Optional<OrderItem> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return repository.save(orderItem);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
