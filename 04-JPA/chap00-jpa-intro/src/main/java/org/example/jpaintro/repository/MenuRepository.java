package org.example.jpaintro.repository;


import org.example.jpaintro.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<엔티티 타입, 식별자 타입>
public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
