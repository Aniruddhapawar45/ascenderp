package com.ascenderp.service;

import com.ascenderp.entity.Inventory;
import com.ascenderp.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    public Inventory getById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Inventory update(Long id, Inventory inventory) {

        Inventory existing = getById(id);

        existing.setProductName(inventory.getProductName());
        existing.setQuantity(inventory.getQuantity());
        existing.setPrice(inventory.getPrice());
        existing.setSupplier(inventory.getSupplier());

        return inventoryRepository.save(existing);
    }

    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }
}