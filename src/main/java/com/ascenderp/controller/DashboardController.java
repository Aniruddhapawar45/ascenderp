package com.ascenderp.controller;

import com.ascenderp.repository.EmployeeRepository;
import com.ascenderp.repository.InventoryRepository;
import com.ascenderp.repository.SaleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final EmployeeRepository employeeRepository;
    private final InventoryRepository inventoryRepository;
    private final SaleRepository saleRepository;

    public DashboardController(
            EmployeeRepository employeeRepository,
            InventoryRepository inventoryRepository,
            SaleRepository saleRepository) {

        this.employeeRepository = employeeRepository;
        this.inventoryRepository = inventoryRepository;
        this.saleRepository = saleRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Map<String, Object> getDashboard() {

        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("totalEmployees",
                employeeRepository.count());

        dashboard.put("totalProducts",
                inventoryRepository.count());

        dashboard.put("totalSales",
                saleRepository.count());

        dashboard.put("totalRevenue",
                saleRepository.getTotalRevenue());

        return dashboard;
    }
}