package com.example.myshop.repository;

import com.example.myshop.domain.Item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByName(String itemName);
}
