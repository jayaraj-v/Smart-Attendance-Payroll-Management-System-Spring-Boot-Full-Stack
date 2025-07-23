package com.example.Explorino_Attendance_WEB_APP.Schedulers;

import com.example.Explorino_Attendance_WEB_APP.Models.Employee;
import com.example.Explorino_Attendance_WEB_APP.Models.LeaveBalance;
import com.example.Explorino_Attendance_WEB_APP.Repository.EmployeeRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.LeaveBalanceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LeaveScheduler
{
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceRepository leaveRepo;

    // Runs on 1st of every month at 00:00 AM
    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void creditMonthlyLeaves() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee emp : employees) {
            LeaveBalance lb = leaveRepo.findById(emp.getEmployeeId())
                    .orElse(new LeaveBalance(emp.getEmployeeId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0));

            String type = emp.getType(); // REGULAR / ALTERNATE / INTERN / PART_TIME

            // Only apply for REGULAR and ALTERNATE
            if (type.equals("REGULAR") || type.equals("ALTERNATE")) {
                // Check joining month to avoid credit before probation ends
                LocalDate doj = LocalDate.parse(emp.getDateOfJoining());
                LocalDate today = LocalDate.now();
                long monthsWorked = java.time.temporal.ChronoUnit.MONTHS.between(doj.withDayOfMonth(1), today.withDayOfMonth(1));

                if (monthsWorked >= 0) {
                    lb.setClAvailable(lb.getClAvailable() + 1);        // 1 Casual per month
                    lb.setPlAvailable(lb.getPlAvailable() + 0.5);      // 0.5 Privilege per month
                    lb.setWlAvailable(1);                               // Only 1 WL each month (non-cumulative)
                }
            }

            // Save updated leave balance
            leaveRepo.save(lb);
        }

        System.out.println("✅ Monthly Leave Balance Updated: " + LocalDate.now());
    }

}
