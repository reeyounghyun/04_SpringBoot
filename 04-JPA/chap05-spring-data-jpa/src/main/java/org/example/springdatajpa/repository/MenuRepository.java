package org.example.springdatajpa.repository;

import org.example.springdatajpa.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("select menu from Menu menu join fetch menu.category")
    List<Menu> findAllWithCategory();
}
