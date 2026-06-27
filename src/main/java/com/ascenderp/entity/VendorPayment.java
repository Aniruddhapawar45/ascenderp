package com.ascenderp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vendor_payments")
public class VendorPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    // optional link to the specific purchase order this payment covers
    @ManyToOne
    @JoinColumn(name = "vendor_order_id")
    private VendorOrder vendorOrder;

    private Double amount;

    private LocalDate paymentDate;

    // e.g. "Bank Transfer", "Cash", "Cheque", "UPI"
    private String paymentMethod;

    // PAID or PENDING
    private String status = "PENDING";

    public VendorPayment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public VendorOrder getVendorOrder() {
        return vendorOrder;
    }

    public void setVendorOrder(VendorOrder vendorOrder) {
        this.vendorOrder = vendorOrder;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
