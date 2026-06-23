package com.ascenderp.controller;

import com.ascenderp.entity.Inventory;
import com.ascenderp.service.InventoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Inventory saveProduct(@RequestBody Inventory inventory) {
        return service.save(inventory);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Inventory> getAllProducts() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Inventory getProduct(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Inventory updateProduct(
            @PathVariable Long id,
            @RequestBody Inventory inventory) {

        return service.update(id, inventory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return "Product deleted successfully";
    }
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Inventory> getLowStockProducts() {
        return service.getLowStockProducts();
    }
}