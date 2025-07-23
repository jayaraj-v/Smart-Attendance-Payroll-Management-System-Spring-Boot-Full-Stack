package com.example.Explorino_Attendance_WEB_APP.Services;

import com.example.Explorino_Attendance_WEB_APP.Models.Attendance;
import com.example.Explorino_Attendance_WEB_APP.Models.Employee;
import com.example.Explorino_Attendance_WEB_APP.Models.SalaryLedger;
import com.example.Explorino_Attendance_WEB_APP.Repository.AttendanceRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.EmployeeRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.SalaryLedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class SalarySchedulerService
{
    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private SalaryLedgerRepository ledgerRepo;

    @Scheduled(cron = "0 0 10 5 * ?") //Every 5th at 10:00 AM
    public void creditSalaries() {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        LocalDate start = previousMonth.atDay(1);
        LocalDate end = previousMonth.atEndOfMonth();
        String monthLabel = previousMonth.getMonth().name() + " " + previousMonth.getYear();

        List<Employee> employees = employeeRepo.findAll();

        for (Employee emp : employees) {
            if (ledgerRepo.existsByEmployeeIdAndMonth(emp.getEmployeeId(), monthLabel)) continue; // prevent double credit

            List<Attendance> records = attendanceRepo.findAll().stream()
                    .filter(a -> a.getEmployeeId().equals(emp.getEmployeeId()))
                    .filter(a -> !a.getDate().isBefore(start) && !a.getDate().isAfter(end))
                    .filter(a -> a.getCheckIn() != null && a.getCheckOut() != null)
                    .toList();

            double hourlyRate = emp.getSalary() / (22 * 7.5); // 🧠 Assuming 22 working days/month, 7.5 hrs/day
            double totalSalary = 0;

            for (Attendance a : records) {
                double hoursWorked = Duration.between(a.getCheckIn(), a.getCheckOut()).toMinutes() / 60.0;
                totalSalary += hoursWorked * hourlyRate;
            }

            SalaryLedger ledger = new SalaryLedger(null, emp.getEmployeeId(), monthLabel,
                    Math.round(totalSalary * 100.0) / 100.0, LocalDate.now());
            ledgerRepo.save(ledger);
        }

        System.out.println("✅ Salary credited for " + monthLabel);
    }
}
