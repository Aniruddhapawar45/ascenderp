package com.ascenderp.service;

import com.ascenderp.entity.Employee;
import com.ascenderp.entity.Payroll;
import com.ascenderp.repository.EmployeeRepository;
import com.ascenderp.repository.PayrollRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollService(PayrollRepository payrollRepository, EmployeeRepository employeeRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
    }

    private double netOf(Payroll p) {
        double base = p.getBaseSalary() != null ? p.getBaseSalary() : 0.0;
        double benefits = p.getBenefits() != null ? p.getBenefits() : 0.0;
        double deductions = p.getDeductions() != null ? p.getDeductions() : 0.0;
        return base + benefits - deductions;
    }

    public Payroll save(Payroll payroll) {

        if (payroll.getEmployee() != null && payroll.getEmployee().getId() != null) {
            Employee employee = new Employee();
            employee.setId(payroll.getEmployee().getId());
            payroll.setEmployee(employee);
        }

        payroll.setNetSalary(netOf(payroll));
        return payrollRepository.save(payroll);
    }

    public List<Payroll> getAll() {
        return payrollRepository.findAll();
    }

    public List<Payroll> getByEmployee(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId);
    }

    public Payroll getById(Long id) {
        return payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll record not found"));
    }

    public Payroll update(Long id, Payroll payroll) {

        Payroll existing = getById(id);

        existing.setBaseSalary(payroll.getBaseSalary());
        existing.setDeductions(payroll.getDeductions());
        existing.setBenefits(payroll.getBenefits());
        existing.setPayPeriodMonth(payroll.getPayPeriodMonth());
        existing.setPayPeriodYear(payroll.getPayPeriodYear());
        existing.setStatus(payroll.getStatus());
        existing.setPaymentDate(payroll.getPaymentDate());

        if (payroll.getEmployee() != null && payroll.getEmployee().getId() != null) {
            Employee employee = new Employee();
            employee.setId(payroll.getEmployee().getId());
            existing.setEmployee(employee);
        }

        existing.setNetSalary(netOf(existing));
        return payrollRepository.save(existing);
    }

    public Payroll markPaid(Long id) {
        Payroll existing = getById(id);
        existing.setStatus("PAID");
        existing.setPaymentDate(LocalDate.now());
        return payrollRepository.save(existing);
    }

    public void delete(Long id) {
        payrollRepository.deleteById(id);
    }

    // Automation: creates a payroll record for every employee that doesn't
    // already have one for the given month/year, pulling baseSalary straight
    // from the Employee record so nobody has to type it in by hand.
    public List<Payroll> generateForPeriod(Integer month, Integer year) {

        List<Employee> employees = employeeRepository.findAll();
        List<Payroll> created = new ArrayList<>();

        for (Employee employee : employees) {

            boolean alreadyExists = payrollRepository
                    .findByEmployeeIdAndPayPeriodMonthAndPayPeriodYear(employee.getId(), month, year)
                    .isPresent();

            if (alreadyExists) {
                continue;
            }

            Payroll payroll = new Payroll();
            payroll.setEmployee(employee);
            payroll.setBaseSalary(employee.getSalary());
            payroll.setDeductions(0.0);
            payroll.setBenefits(0.0);
            payroll.setPayPeriodMonth(month);
            payroll.setPayPeriodYear(year);
            payroll.setStatus("PENDING");
            payroll.setNetSalary(netOf(payroll));

            created.add(payrollRepository.save(payroll));
        }

        return created;
    }
}
