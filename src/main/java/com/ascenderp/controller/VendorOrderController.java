package com.ascenderp.controller;

import com.ascenderp.entity.VendorOrder;
import com.ascenderp.service.VendorOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor-orders")
public class VendorOrderController {

    private final VendorOrderService vendorOrderService;

    public VendorOrderController(VendorOrderService vendorOrderService) {
        this.vendorOrderService = vendorOrderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public VendorOrder createOrder(@RequestBody VendorOrder order) {
        return vendorOrderService.save(order);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<VendorOrder> getAllOrders() {
        return vendorOrderService.getAll();
    }

    @GetMapping("/vendor/{vendorId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<VendorOrder> getOrdersByVendor(@PathVariable Long vendorId) {
        return vendorOrderService.getByVendor(vendorId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public VendorOrder getOrder(@PathVariable Long id) {
        return vendorOrderService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VendorOrder updateOrder(@PathVariable Long id, @RequestBody VendorOrder order) {
        return vendorOrderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteOrder(@PathVariable Long id) {
        vendorOrderService.delete(id);
        return "Vendor order deleted successfully";
    }
}
