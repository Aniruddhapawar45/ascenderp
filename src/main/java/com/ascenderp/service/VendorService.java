package com.ascenderp.service;

import com.ascenderp.entity.Vendor;
import com.ascenderp.entity.VendorOrder;
import com.ascenderp.entity.VendorPayment;
import com.ascenderp.repository.VendorOrderRepository;
import com.ascenderp.repository.VendorPaymentRepository;
import com.ascenderp.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final VendorOrderRepository vendorOrderRepository;
    private final VendorPaymentRepository vendorPaymentRepository;

    public VendorService(
            VendorRepository vendorRepository,
            VendorOrderRepository vendorOrderRepository,
            VendorPaymentRepository vendorPaymentRepository) {

        this.vendorRepository = vendorRepository;
        this.vendorOrderRepository = vendorOrderRepository;
        this.vendorPaymentRepository = vendorPaymentRepository;
    }

    public Vendor save(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public List<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    public Vendor getById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    public Vendor update(Long id, Vendor vendor) {

        Vendor existing = getById(id);

        existing.setName(vendor.getName());
        existing.setContactPerson(vendor.getContactPerson());
        existing.setEmail(vendor.getEmail());
        existing.setPhone(vendor.getPhone());
        existing.setAddress(vendor.getAddress());
        existing.setGstNumber(vendor.getGstNumber());
        existing.setStatus(vendor.getStatus());

        return vendorRepository.save(existing);
    }

    public void delete(Long id) {
        vendorRepository.deleteById(id);
    }

    // Computed performance metrics for a single vendor — not stored,
    // derived from their order and payment history.
    public Map<String, Object> getPerformanceMetrics(Long vendorId) {

        // ensures a 404-style error if the vendor doesn't exist
        getById(vendorId);

        List<VendorOrder> orders = vendorOrderRepository.findByVendorId(vendorId);
        List<VendorPayment> payments = vendorPaymentRepository.findByVendorId(vendorId);

        long totalOrders = orders.size();

        long deliveredOrders = orders.stream()
                .filter(o -> "DELIVERED".equalsIgnoreCase(o.getStatus()))
                .count();

        long onTimeDeliveries = orders.stream()
                .filter(o -> "DELIVERED".equalsIgnoreCase(o.getStatus())
                        && o.getActualDeliveryDate() != null
                        && o.getExpectedDeliveryDate() != null
                        && !o.getActualDeliveryDate().isAfter(o.getExpectedDeliveryDate()))
                .count();

        double onTimeDeliveryRate = deliveredOrders == 0
                ? 0.0
                : (onTimeDeliveries * 100.0) / deliveredOrders;

        double totalOrderValue = orders.stream()
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0.0)
                .sum();

        double totalPaid = payments.stream()
                .filter(p -> "PAID".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        double totalOutstanding = totalOrderValue - totalPaid;

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalOrders", totalOrders);
        metrics.put("deliveredOrders", deliveredOrders);
        metrics.put("onTimeDeliveryRate", Math.round(onTimeDeliveryRate * 10) / 10.0);
        metrics.put("totalOrderValue", totalOrderValue);
        metrics.put("totalPaid", totalPaid);
        metrics.put("totalOutstanding", totalOutstanding);

        return metrics;
    }
}
