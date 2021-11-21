package com.anderfred.assignmenttask.service;

import com.anderfred.assignmenttask.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAll();
    Optional<Category> findById(long id);
    Category save(Category category);
    void deleteAll();
    void deleteById(long id);
}
