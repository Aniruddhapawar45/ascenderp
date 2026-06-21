package com.ascenderp.controller;

import com.ascenderp.entity.Employee;
import com.ascenderp.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Employee> getEmployees() {
        return service.getAll();
    }

    @PostMapping
    public Employee saveEmployee(@RequestBody Employee employee) {
        return service.save(employee);
    }
    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return service.getById(id);
    }
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        service.delete(id);
        return "Employee deleted successfully";
    }
    @PutMapping("/{id}")
    public Employee updateEmployee(        // update empolyee
            @PathVariable Long id,
            @RequestBody Employee employee) {

        return service.update(id, employee);
    }
}