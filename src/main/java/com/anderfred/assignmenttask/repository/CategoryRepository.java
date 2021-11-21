package com.anderfred.assignmenttask.repository;

import com.anderfred.assignmenttask.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
