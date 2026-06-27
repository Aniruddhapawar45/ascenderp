package com.ascenderp.controller;

import com.ascenderp.entity.VendorPayment;
import com.ascenderp.service.VendorPaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendor-payments")
public class VendorPaymentController {

    private final VendorPaymentService vendorPaymentService;

    public VendorPaymentController(VendorPaymentService vendorPaymentService) {
        this.vendorPaymentService = vendorPaymentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public VendorPayment createPayment(@RequestBody VendorPayment payment) {
        return vendorPaymentService.save(payment);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<VendorPayment> getAllPayments() {
        return vendorPaymentService.getAll();
    }

    @GetMapping("/vendor/{vendorId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<VendorPayment> getPaymentsByVendor(@PathVariable Long vendorId) {
        return vendorPaymentService.getByVendor(vendorId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public VendorPayment getPayment(@PathVariable Long id) {
        return vendorPaymentService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VendorPayment updatePayment(@PathVariable Long id, @RequestBody VendorPayment payment) {
        return vendorPaymentService.update(id, payment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePayment(@PathVariable Long id) {
        vendorPaymentService.delete(id);
        return "Vendor payment deleted successfully";
    }
}
