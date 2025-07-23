package com.example.Explorino_Attendance_WEB_APP.Controllers;

import com.example.Explorino_Attendance_WEB_APP.Models.Employee;
import com.example.Explorino_Attendance_WEB_APP.Models.SalaryLedger;
import com.example.Explorino_Attendance_WEB_APP.Repository.AttendanceRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.EmployeeRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.SalaryLedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
@RestController
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private SalaryLedgerRepository ledgerRepo;

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    // ✅ Get salary history (already implemented)
    @GetMapping("/history/{empId}")
    public List<SalaryLedger> getSalaryHistory(@PathVariable String empId) {
        return ledgerRepo.findAll().stream()
                .filter(s -> s.getEmployeeId().equals(empId))
                .toList();
    }

    // ✅ Process salary for a given month (NEW)
    @PostMapping("/process/{month}") // Example: 2025-07
    public ResponseEntity<String> processPayroll(@PathVariable String month) {
        YearMonth ym = YearMonth.parse(month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<Employee> allEmployees = employeeRepo.findAll();

        for (Employee emp : allEmployees) {
            // Avoid duplicate processing
            boolean alreadyCredited = ledgerRepo.existsByEmployeeIdAndMonth(emp.getEmployeeId(), month);
            if (alreadyCredited) continue;

            double totalSalary = attendanceRepo.findByEmployeeIdAndDateBetween(emp.getEmployeeId(), start, end)
                    .stream()
                    .mapToDouble(a -> a.getDaySalary() != null ? a.getDaySalary() : 0.0)
                    .sum();

            SalaryLedger ledger = new SalaryLedger();
            ledger.setEmployeeId(emp.getEmployeeId());
            ledger.setCreditedDate(LocalDate.now());
            ledger.setMonth(month);
            ledger.setTotalSalary(totalSalary);

            ledgerRepo.save(ledger);
        }

        return ResponseEntity.ok("✅ Payroll processed for " + month);
    }

}
