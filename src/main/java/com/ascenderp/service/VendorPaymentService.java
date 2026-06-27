package com.ascenderp.service;

import com.ascenderp.entity.Vendor;
import com.ascenderp.entity.VendorOrder;
import com.ascenderp.entity.VendorPayment;
import com.ascenderp.repository.VendorPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorPaymentService {

    private final VendorPaymentRepository vendorPaymentRepository;

    public VendorPaymentService(VendorPaymentRepository vendorPaymentRepository) {
        this.vendorPaymentRepository = vendorPaymentRepository;
    }

    private void linkReferences(VendorPayment payment) {
        if (payment.getVendor() != null && payment.getVendor().getId() != null) {
            Vendor vendor = new Vendor();
            vendor.setId(payment.getVendor().getId());
            payment.setVendor(vendor);
        }
        if (payment.getVendorOrder() != null && payment.getVendorOrder().getId() != null) {
            VendorOrder order = new VendorOrder();
            order.setId(payment.getVendorOrder().getId());
            payment.setVendorOrder(order);
        } else {
            payment.setVendorOrder(null);
        }
    }

    public VendorPayment save(VendorPayment payment) {
        linkReferences(payment);
        return vendorPaymentRepository.save(payment);
    }

    public List<VendorPayment> getAll() {
        return vendorPaymentRepository.findAll();
    }

    public List<VendorPayment> getByVendor(Long vendorId) {
        return vendorPaymentRepository.findByVendorId(vendorId);
    }

    public VendorPayment getById(Long id) {
        return vendorPaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor payment not found"));
    }

    public VendorPayment update(Long id, VendorPayment payment) {

        VendorPayment existing = getById(id);

        existing.setAmount(payment.getAmount());
        existing.setPaymentDate(payment.getPaymentDate());
        existing.setPaymentMethod(payment.getPaymentMethod());
        existing.setStatus(payment.getStatus());

        if (payment.getVendor() != null && payment.getVendor().getId() != null) {
            Vendor vendor = new Vendor();
            vendor.setId(payment.getVendor().getId());
            existing.setVendor(vendor);
        }
        if (payment.getVendorOrder() != null && payment.getVendorOrder().getId() != null) {
            VendorOrder order = new VendorOrder();
            order.setId(payment.getVendorOrder().getId());
            existing.setVendorOrder(order);
        }

        return vendorPaymentRepository.save(existing);
    }

    public void delete(Long id) {
        vendorPaymentRepository.deleteById(id);
    }
}
