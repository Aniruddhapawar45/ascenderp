package com.ascenderp.controller;

import com.ascenderp.entity.Payroll;
import com.ascenderp.service.PayrollService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Payroll createPayroll(@RequestBody Payroll payroll) {
        return payrollService.save(payroll);
    }

    // body: { "month": 6, "year": 2026 }
    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Payroll> generatePayroll(@RequestBody Map<String, Integer> period) {
        return payrollService.generateForPeriod(period.get("month"), period.get("year"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Payroll> getAllPayroll() {
        return payrollService.getAll();
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Payroll> getPayrollByEmployee(@PathVariable Long employeeId) {
        return payrollService.getByEmployee(employeeId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Payroll getPayroll(@PathVariable Long id) {
        return payrollService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Payroll updatePayroll(@PathVariable Long id, @RequestBody Payroll payroll) {
        return payrollService.update(id, payroll);
    }

    @PutMapping("/{id}/mark-paid")
    @PreAuthorize("hasRole('ADMIN')")
    public Payroll markPaid(@PathVariable Long id) {
        return payrollService.markPaid(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePayroll(@PathVariable Long id) {
        payrollService.delete(id);
        return "Payroll record deleted successfully";
    }
}
