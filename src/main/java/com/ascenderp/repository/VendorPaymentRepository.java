package com.ascenderp.repository;

import com.ascenderp.entity.VendorPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorPaymentRepository extends JpaRepository<VendorPayment, Long> {

    List<VendorPayment> findByVendorId(Long vendorId);
}
