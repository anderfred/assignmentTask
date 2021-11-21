package com.anderfred.assignmenttask.service;

import com.anderfred.assignmenttask.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAll();
    Optional<Product> findById(long id);
    Product save(Product product);
    void deleteAll();
    void deleteById(long id);

}
