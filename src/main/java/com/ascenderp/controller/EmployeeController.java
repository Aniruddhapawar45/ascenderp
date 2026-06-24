package com.ascenderp.controller;

import com.ascenderp.entity.Employee;
import com.ascenderp.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Employee> getEmployees() {
        return service.getAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Employee saveEmployee(@RequestBody Employee employee) {
        return service.save(employee);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Employee getEmployee(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteEmployee(@PathVariable Long id) {
        service.delete(id);
        return "Employee deleted successfully";
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Employee updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        return service.update(id, employee);
    }
}