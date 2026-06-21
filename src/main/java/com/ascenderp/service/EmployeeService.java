package com.ascenderp.service;

import com.ascenderp.entity.Employee;
import com.ascenderp.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }
    public Employee getById(Long id) {                           //Add Get By ID
        return repository.findById(id).orElse(null);
    }
    public void delete(Long id) {                               //for Delete
        repository.deleteById(id);
    }
    public Employee update(Long id, Employee employee) {           //for  Update

        Employee existing = repository.findById(id).orElse(null);

        if (existing != null) {
            existing.setName(employee.getName());
            existing.setEmail(employee.getEmail());
            existing.setDepartment(employee.getDepartment());
            existing.setSalary(employee.getSalary());

            return repository.save(existing);
        }

        return null;
    }
}