package com.ascenderp.controller;

import com.ascenderp.entity.Customer;
import com.ascenderp.service.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Customer> getAllCustomers() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Customer updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer) {
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return "Customer deleted successfully";
    }
}