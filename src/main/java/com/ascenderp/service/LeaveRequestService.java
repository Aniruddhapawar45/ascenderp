package com.ascenderp.service;

import com.ascenderp.entity.Employee;
import com.ascenderp.entity.LeaveRequest;
import com.ascenderp.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    private Integer computeDays(LeaveRequest leave) {
        if (leave.getStartDate() == null || leave.getEndDate() == null) {
            return null;
        }
        long days = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        return (int) Math.max(days, 0);
    }

    public LeaveRequest save(LeaveRequest leave) {

        if (leave.getEmployee() != null && leave.getEmployee().getId() != null) {
            Employee employee = new Employee();
            employee.setId(leave.getEmployee().getId());
            leave.setEmployee(employee);
        }

        if (leave.getAppliedDate() == null) {
            leave.setAppliedDate(LocalDate.now());
        }
        if (leave.getStatus() == null || leave.getStatus().isBlank()) {
            leave.setStatus("PENDING");
        }

        leave.setNumberOfDays(computeDays(leave));
        return leaveRequestRepository.save(leave);
    }

    public List<LeaveRequest> getAll() {
        return leaveRequestRepository.findAll();
    }

    public List<LeaveRequest> getByEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    public LeaveRequest getById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
    }

    public LeaveRequest update(Long id, LeaveRequest leave) {

        LeaveRequest existing = getById(id);

        existing.setLeaveType(leave.getLeaveType());
        existing.setStartDate(leave.getStartDate());
        existing.setEndDate(leave.getEndDate());
        existing.setReason(leave.getReason());
        existing.setStatus(leave.getStatus());
        existing.setRemarks(leave.getRemarks());

        if (leave.getEmployee() != null && leave.getEmployee().getId() != null) {
            Employee employee = new Employee();
            employee.setId(leave.getEmployee().getId());
            existing.setEmployee(employee);
        }

        existing.setNumberOfDays(computeDays(existing));
        return leaveRequestRepository.save(existing);
    }

    public LeaveRequest approve(Long id, String remarks) {
        LeaveRequest existing = getById(id);
        existing.setStatus("APPROVED");
        existing.setRemarks(remarks);
        return leaveRequestRepository.save(existing);
    }

    public LeaveRequest reject(Long id, String remarks) {
        LeaveRequest existing = getById(id);
        existing.setStatus("REJECTED");
        existing.setRemarks(remarks);
        return leaveRequestRepository.save(existing);
    }

    public void delete(Long id) {
        leaveRequestRepository.deleteById(id);
    }

    // Leave report: counts by status and by leave type, for a quick
    // dashboard-style summary rather than scrolling the raw table.
    public Map<String, Object> getSummary() {

        List<LeaveRequest> all = leaveRequestRepository.findAll();

        long pending = all.stream().filter(l -> "PENDING".equalsIgnoreCase(l.getStatus())).count();
        long approved = all.stream().filter(l -> "APPROVED".equalsIgnoreCase(l.getStatus())).count();
        long rejected = all.stream().filter(l -> "REJECTED".equalsIgnoreCase(l.getStatus())).count();

        Map<String, Long> byType = all.stream()
                .filter(l -> l.getLeaveType() != null)
                .collect(Collectors.groupingBy(LeaveRequest::getLeaveType, Collectors.counting()));

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRequests", (long) all.size());
        summary.put("pending", pending);
        summary.put("approved", approved);
        summary.put("rejected", rejected);
        summary.put("byType", byType);

        return summary;
    }
}
