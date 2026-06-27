package com.ascenderp.service;

import com.ascenderp.entity.Vendor;
import com.ascenderp.entity.VendorOrder;
import com.ascenderp.repository.VendorOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorOrderService {

    private final VendorOrderRepository vendorOrderRepository;

    public VendorOrderService(VendorOrderRepository vendorOrderRepository) {
        this.vendorOrderRepository = vendorOrderRepository;
    }

    public VendorOrder save(VendorOrder order) {

        if (order.getVendor() != null && order.getVendor().getId() != null) {
            Vendor vendor = new Vendor();
            vendor.setId(order.getVendor().getId());
            order.setVendor(vendor);
        }

        return vendorOrderRepository.save(order);
    }

    public List<VendorOrder> getAll() {
        return vendorOrderRepository.findAll();
    }

    public List<VendorOrder> getByVendor(Long vendorId) {
        return vendorOrderRepository.findByVendorId(vendorId);
    }

    public VendorOrder getById(Long id) {
        return vendorOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor order not found"));
    }

    public VendorOrder update(Long id, VendorOrder order) {

        VendorOrder existing = getById(id);

        existing.setProductName(order.getProductName());
        existing.setQuantity(order.getQuantity());
        existing.setUnitPrice(order.getUnitPrice());
        existing.setTotalAmount(order.getTotalAmount());
        existing.setOrderDate(order.getOrderDate());
        existing.setExpectedDeliveryDate(order.getExpectedDeliveryDate());
        existing.setActualDeliveryDate(order.getActualDeliveryDate());
        existing.setStatus(order.getStatus());

        if (order.getVendor() != null && order.getVendor().getId() != null) {
            Vendor vendor = new Vendor();
            vendor.setId(order.getVendor().getId());
            existing.setVendor(vendor);
        }

        return vendorOrderRepository.save(existing);
    }

    public void delete(Long id) {
        vendorOrderRepository.deleteById(id);
    }
}
