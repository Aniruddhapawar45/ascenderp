package com.ascenderp.service;

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

    public Sale save(Sale sale) {
        return saleRepository.save(sale);
    }

    public List<Sale> getAll() {
        return saleRepository.findAll();
    }

    public Sale getById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
    }

    public Sale update(Long id, Sale sale) {

        Sale existing = getById(id);

        existing.setProductName(sale.getProductName());
        existing.setQuantity(sale.getQuantity());
        existing.setTotalAmount(sale.getTotalAmount());
        existing.setCustomerName(sale.getCustomerName());
        existing.setSaleDate(sale.getSaleDate());

        return saleRepository.save(existing);
    }

    public void delete(Long id) {
        saleRepository.deleteById(id);
    }
}