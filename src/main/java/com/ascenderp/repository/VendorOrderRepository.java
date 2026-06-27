package com.ascenderp.repository;

import com.ascenderp.entity.VendorOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorOrderRepository extends JpaRepository<VendorOrder, Long> {

    List<VendorOrder> findByVendorId(Long vendorId);
}
