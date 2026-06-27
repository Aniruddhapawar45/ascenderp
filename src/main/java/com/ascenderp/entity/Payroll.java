package com.ascenderp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payrolls")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private Double baseSalary;

    private Double deductions = 0.0;

    private Double benefits = 0.0;

    // computed as baseSalary + benefits - deductions, kept in sync by the service
    private Double netSalary;

    private Integer payPeriodMonth;

    private Integer payPeriodYear;

    // PENDING or PAID
    private String status = "PENDING";

    private LocalDate paymentDate;

    public Payroll() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Double getDeductions() {
        return deductions;
    }

    public void setDeductions(Double deductions) {
        this.deductions = deductions;
    }

    public Double getBenefits() {
        return benefits;
    }

    public void setBenefits(Double benefits) {
        this.benefits = benefits;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public Integer getPayPeriodMonth() {
        return payPeriodMonth;
    }

    public void setPayPeriodMonth(Integer payPeriodMonth) {
        this.payPeriodMonth = payPeriodMonth;
    }

    public Integer getPayPeriodYear() {
        return payPeriodYear;
    }

    public void setPayPeriodYear(Integer payPeriodYear) {
        this.payPeriodYear = payPeriodYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
