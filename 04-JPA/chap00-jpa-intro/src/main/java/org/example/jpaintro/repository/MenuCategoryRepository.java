package org.example.jpaintro.repository;

import org.example.jpaintro.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<엔티티 타입, 식별자 타입>
public interface MenuCategoryRepository extends JpaRepository<Category, Integer> {
}
