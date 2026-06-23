package com.ascenderp.controller;

import com.ascenderp.entity.Sale;
import com.ascenderp.service.SaleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Sale saveSale(@RequestBody Sale sale) {
        return service.save(sale);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Sale> getAllSales() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Sale getSale(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Sale updateSale(
            @PathVariable Long id,
            @RequestBody Sale sale) {

        return service.update(id, sale);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSale(@PathVariable Long id) {
        service.delete(id);
        return "Sale deleted successfully";
    }
}