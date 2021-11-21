package com.anderfred.assignmenttask.service.impl;

import com.anderfred.assignmenttask.model.Category;
import com.anderfred.assignmenttask.repository.CategoryRepository;
import com.anderfred.assignmenttask.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Category> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Optional<Category> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Category save(Category category) {
        return repository.save(category);
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
