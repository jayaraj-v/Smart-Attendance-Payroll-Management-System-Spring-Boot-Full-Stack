package com.example.Explorino_Attendance_WEB_APP.Controllers;

import com.example.Explorino_Attendance_WEB_APP.Models.Employee;
import com.example.Explorino_Attendance_WEB_APP.Models.LeaveBalance;
import com.example.Explorino_Attendance_WEB_APP.Models.LeaveRequest;
import com.example.Explorino_Attendance_WEB_APP.Models.LeaveRequestDTO;
import com.example.Explorino_Attendance_WEB_APP.Repository.EmployeeRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.LeaveBalanceRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/hr/leaves")
@RestController
public class HrLeaveController
{

    @Autowired
    private LeaveRequestRepository leaveRequestRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private LeaveBalanceRepository balanceRepo;

    @GetMapping("/pending")
    public List<LeaveRequestDTO> getPendingRequests()
    {
        return leaveRequestRepo.findByStatus("PENDING")
                .stream()
                .map(req ->
                {
                    String name = employeeRepo.findById(req.getEmployeeId())
                            .map(Employee::getName)
                            .orElse("Unknown");
                    return new LeaveRequestDTO(
                            req.getLeaveId(),
                            req.getEmployeeId(),
                            name,
                            req.getLeaveType(),
                            req.getReason(),
                            req.getDays(),
                            req.getSubmissionDate()
                    );
                }).collect(Collectors.toList());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveLeave(@PathVariable Long id)
    {
        LeaveRequest req = leaveRequestRepo.findById(id).orElse(null);
        if (req == null) return ResponseEntity.notFound().build();

        // 🧠 New Expiry Check
        if (List.of("WL", "CO", "DL").contains(req.getLeaveType())) {
            boolean expired = isLeaveExpired(req.getLeaveType(), req.getSubmissionDate(), LocalDate.now());
            if (expired) {
                return ResponseEntity.status(400).body("❌ Leave has expired as per company policy.");
            }
        }

        LeaveBalance balance = balanceRepo.findById(req.getEmployeeId())
                .orElse(new LeaveBalance(req.getEmployeeId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0));

        boolean valid = switch (req.getLeaveType())
        {
            case "CL" -> balance.getClAvailable() >= req.getDays();
            case "PL" -> balance.getPlAvailable() >= req.getDays();
            case "WL" -> balance.getWlAvailable() >= req.getDays();
            case "CO" -> balance.getCoAvailable() >= req.getDays();
            case "DL" -> balance.getDlAvailable() >= req.getDays();
            case "ML" -> balance.getMlAvailable() >= req.getDays();
            case "PATL" -> balance.getPatlAvailable() >= req.getDays();
            default -> false;
        };

        if (!valid)
            return ResponseEntity.status(400).body("❌ Insufficient leave balance!");

        switch (req.getLeaveType())
        {
            case "CL" ->
            {
                balance.setClAvailable(balance.getClAvailable() - req.getDays());
                balance.setClUsed(balance.getClUsed() + req.getDays());
            }
            case "PL" ->
            {
                balance.setPlAvailable(balance.getPlAvailable() - req.getDays());
                balance.setPlUsed(balance.getPlUsed() + req.getDays());
            }
            case "WL" ->
            {
                balance.setWlAvailable(balance.getWlAvailable() - req.getDays());
                balance.setWlUsed(balance.getWlUsed() + req.getDays());
            }
            case "CO" ->
            {
                balance.setCoAvailable(balance.getCoAvailable() - req.getDays());
                balance.setCoUsed(balance.getCoUsed() + req.getDays());
            }
            case "DL" ->
            {
                balance.setDlAvailable(balance.getDlAvailable() - req.getDays());
                balance.setDlUsed(balance.getDlUsed() + req.getDays());
            }
            case "ML" ->
            {
                balance.setMlAvailable(balance.getMlAvailable() - req.getDays());
                balance.setMlUsed(balance.getMlUsed() + req.getDays());
            }
            case "PATL" ->
            {
                balance.setPatlAvailable(balance.getPatlAvailable() - req.getDays());
                balance.setPatlUsed(balance.getPatlUsed() + req.getDays());
            }
        }

        balanceRepo.save(balance);
        req.setStatus("APPROVED");
        leaveRequestRepo.save(req);
        return ResponseEntity.ok("✅ Leave approved.");
    }



    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectLeave(@PathVariable Long id)
    {
        LeaveRequest req = leaveRequestRepo.findById(id).orElse(null);
        if (req == null) return ResponseEntity.notFound().build();
        req.setStatus("REJECTED");
        leaveRequestRepo.save(req);
        return ResponseEntity.ok("❌ Leave rejected.");
    }

    private boolean isLeaveExpired(String leaveType, LocalDate earnedDate, LocalDate today) {
        switch (leaveType) {
            case "WL":
                return !(earnedDate.getMonth() == today.getMonth() && earnedDate.getYear() == today.getYear());
            case "CO":
            case "DL":
                YearMonth earnedMonth = YearMonth.from(earnedDate);
                YearMonth expiryMonth = earnedMonth.plusMonths(1);
                YearMonth currentMonth = YearMonth.from(today);
                return currentMonth.isAfter(expiryMonth); // if current > expiry → expired
            default:
                return false; // CL, PL, ML, PATL etc. don't expire this way
        }
    }

}
