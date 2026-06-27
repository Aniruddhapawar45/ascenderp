package com.ascenderp.controller;

import com.ascenderp.entity.Vendor;
import com.ascenderp.service.VendorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Vendor createVendor(@RequestBody Vendor vendor) {
        return vendorService.save(vendor);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Vendor> getAllVendors() {
        return vendorService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Vendor getVendor(@PathVariable Long id) {
        return vendorService.getById(id);
    }

    @GetMapping("/{id}/performance")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Map<String, Object> getVendorPerformance(@PathVariable Long id) {
        return vendorService.getPerformanceMetrics(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Vendor updateVendor(@PathVariable Long id, @RequestBody Vendor vendor) {
        return vendorService.update(id, vendor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteVendor(@PathVariable Long id) {
        vendorService.delete(id);
        return "Vendor deleted successfully";
    }
}
