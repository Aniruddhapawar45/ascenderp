package com.ascenderp.repository;

import com.ascenderp.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {
}