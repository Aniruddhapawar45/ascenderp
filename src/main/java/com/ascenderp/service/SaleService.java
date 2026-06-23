package com.ascenderp.service;

import com.ascenderp.entity.Customer;
import com.ascenderp.entity.Sale;
import com.ascenderp.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    // ✅ CREATE SALE (with customer link fix)
    public Sale save(Sale sale) {

        if (sale.getCustomer() != null &&
                sale.getCustomer().getId() != null) {

            Customer customer = new Customer();
            customer.setId(sale.getCustomer().getId());

            sale.setCustomer(customer);
        }

        return saleRepository.save(sale);
    }

    // ✅ GET ALL SALES
    public List<Sale> getAll() {
        return saleRepository.findAll();
    }

    // ✅ GET BY ID
    public Sale getById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
    }

    // ✅ UPDATE SALE
    public Sale update(Long id, Sale sale) {

        Sale existing = getById(id);

        existing.setProductName(sale.getProductName());
        existing.setQuantity(sale.getQuantity());
        existing.setTotalAmount(sale.getTotalAmount());
        existing.setSaleDate(sale.getSaleDate());

        // update customer if present
        if (sale.getCustomer() != null &&
                sale.getCustomer().getId() != null) {

            Customer customer = new Customer();
            customer.setId(sale.getCustomer().getId());

            existing.setCustomer(customer);
        }

        return saleRepository.save(existing);
    }

    // ✅ DELETE SALE
    public void delete(Long id) {
        saleRepository.deleteById(id);
    }
}