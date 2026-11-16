package org.example.springdatajpa.repository;

import org.example.springdatajpa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
