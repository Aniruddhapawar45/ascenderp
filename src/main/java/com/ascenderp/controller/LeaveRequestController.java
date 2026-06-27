package com.ascenderp.controller;

import com.ascenderp.entity.Employee;
import com.ascenderp.entity.LeaveRequest;
import com.ascenderp.entity.User;
import com.ascenderp.service.LeaveRequestService;
import com.ascenderp.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leaves")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final UserService userService;

    public LeaveRequestController(LeaveRequestService leaveRequestService, UserService userService) {
        this.leaveRequestService = leaveRequestService;
        this.userService = userService;
    }

    private User currentUser(Authentication authentication) {
        return userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Admins can file a leave request on behalf of any employee, exactly
    // as supplied in the request body. Regular users can only file for
    // themselves — whatever employee they sent in the body is ignored and
    // replaced with their own linked employee record.
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public LeaveRequest createLeave(@RequestBody LeaveRequest leave, Authentication authentication) {

        User user = currentUser(authentication);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return leaveRequestService.save(leave);
        }

        if (user.getEmployee() == null) {
            throw new RuntimeException("Your account isn't linked to an employee record yet. Ask an admin to link it.");
        }

        Employee self = new Employee();
        self.setId(user.getEmployee().getId());
        leave.setEmployee(self);
        leave.setStatus("PENDING"); // users can't self-approve

        return leaveRequestService.save(leave);
    }

    // Self-service: only the requests belonging to the signed-in user's
    // own linked employee record.
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<LeaveRequest> getMyLeaves(Authentication authentication) {
        User user = currentUser(authentication);
        if (user.getEmployee() == null) {
            return Collections.emptyList();
        }
        return leaveRequestService.getByEmployee(user.getEmployee().getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<LeaveRequest> getAllLeaves() {
        return leaveRequestService.getAll();
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<LeaveRequest> getLeavesByEmployee(@PathVariable Long employeeId) {
        return leaveRequestService.getByEmployee(employeeId);
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Map<String, Object> getLeaveSummary() {
        return leaveRequestService.getSummary();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public LeaveRequest getLeave(@PathVariable Long id) {
        return leaveRequestService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public LeaveRequest updateLeave(@PathVariable Long id, @RequestBody LeaveRequest leave) {
        return leaveRequestService.update(id, leave);
    }

    // body: { "remarks": "Approved, enjoy your time off" } — remarks is optional
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public LeaveRequest approveLeave(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String remarks = body != null ? body.get("remarks") : null;
        return leaveRequestService.approve(id, remarks);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public LeaveRequest rejectLeave(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String remarks = body != null ? body.get("remarks") : null;
        return leaveRequestService.reject(id, remarks);
    }

    // Admins can delete any leave request. Regular users can only cancel
    // their own request, and only while it's still PENDING.
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String deleteLeave(@PathVariable Long id, Authentication authentication) {

        User user = currentUser(authentication);

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            LeaveRequest existing = leaveRequestService.getById(id);
            boolean isOwnRequest = user.getEmployee() != null
                    && existing.getEmployee() != null
                    && user.getEmployee().getId().equals(existing.getEmployee().getId());

            if (!isOwnRequest || !"PENDING".equalsIgnoreCase(existing.getStatus())) {
                throw new RuntimeException("You can only cancel your own pending leave requests.");
            }
        }

        leaveRequestService.delete(id);
        return "Leave request deleted successfully";
    }
}
