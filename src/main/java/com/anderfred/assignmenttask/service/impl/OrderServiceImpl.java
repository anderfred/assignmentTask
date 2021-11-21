package com.anderfred.assignmenttask.service.impl;

import com.anderfred.assignmenttask.adapter.OrdersReportAdapter;
import com.anderfred.assignmenttask.elasticrepo.OrderSearchRepository;
import com.anderfred.assignmenttask.model.Order;
import com.anderfred.assignmenttask.repository.OrderRepository;
import com.anderfred.assignmenttask.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository repository;
    OrderSearchRepository searchRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, OrderSearchRepository searchRepository) {
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<Order> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Optional<Order> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Order save(Order order) {
        Order saved = repository.save(order);
        searchRepository.save(order);
        return saved;
    }

    @Override
    public void deleteAll() {
        searchRepository.deleteAll();
        repository.deleteAll();
    }

    @Override
    public void deleteById(long id) {
        searchRepository.deleteById(id);
        repository.deleteById(id);
    }

    @Override
    public String findReportData() {
        OrdersReportAdapter adapter = new OrdersReportAdapter(repository.findReportData());
        return adapter.getReport();
    }

    @Override
    public Set<Order> findByProductNameCustom(String name, Pageable pageable) {
        Page<Order> page = searchRepository.findByProductNameCustom(name, pageable);
        return page.get().collect(Collectors.toUnmodifiableSet());
    }
}
