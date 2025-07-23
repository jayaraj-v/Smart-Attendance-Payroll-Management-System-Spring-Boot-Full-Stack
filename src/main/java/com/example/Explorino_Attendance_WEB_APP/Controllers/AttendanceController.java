package com.example.Explorino_Attendance_WEB_APP.Controllers;

import com.example.Explorino_Attendance_WEB_APP.Models.*;
import com.example.Explorino_Attendance_WEB_APP.Repository.AttendanceRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController
{
    @Autowired
    private AttendanceRepository attendanceRepo;
    @Autowired
    private EmployeeRepository employeeRepository;

    private static final double OFFICE_LAT = 10.96446130957028;   // Replace with your actual latitude
    private static final double OFFICE_LON = 78.01419038868525;   // Replace with your actual longitude
    private static final double MAX_RADIUS_METERS = 300;




    private boolean isInsideOffice(double userLat, double userLon)
    {
        final int R = 6371000; // Earth's radius in meters
        double dLat = Math.toRadians(userLat - OFFICE_LAT);
        double dLon = Math.toRadians(userLon - OFFICE_LON);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(OFFICE_LAT)) * Math.cos(Math.toRadians(userLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance <= MAX_RADIUS_METERS;
    }

    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(@RequestBody AttendanceRequest req, Authentication auth) {
        String employeeId = auth.getName(); // Extract employee ID from logged-in user
        String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        // ⛔ Geofence Check
        if (!isInsideOffice(req.getLatitude(), req.getLongitude())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("❌ Attendance not allowed! Please ensure you're inside the company premises.");
        }

        // 🕒 Parse Date and Time
        LocalDate date = LocalDate.parse(req.getDate());
        LocalTime time = LocalTime.parse(req.getTime());

        // 🌐 Capture IP and User-Agent
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String ipAddress = servletRequest.getRemoteAddr();
        String userAgent = servletRequest.getHeader("User-Agent");

        // 🔍 Fetch or Create Today's Attendance Record
        Attendance attendance = attendanceRepo.findByEmployeeIdAndDate(employeeId, date)
                .orElseGet(() -> {
                    Attendance a = new Attendance();
                    a.setEmployeeId(employeeId);
                    a.setRole(role);
                    a.setDate(date);
                    return a;
                });

        // ✅ Handle Check-In
        if ("IN".equalsIgnoreCase(req.getType())) {
            if (attendance.getCheckIn() != null) {
                return ResponseEntity.badRequest().body("⚠️ You have already checked in today.");
            }

            attendance.setCheckIn(time);
            attendance.setLatitude(req.getLatitude());
            attendance.setLongitude(req.getLongitude());
            attendance.setIpAddress(ipAddress);
            attendance.setUserAgent(userAgent);

            attendanceRepo.save(attendance);
            return ResponseEntity.ok("✅ Check-in successful!");

            // ✅ Handle Check-Out
        } else
        if ("OUT".equalsIgnoreCase(req.getType())) {
            if (attendance.getCheckIn() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("⚠️ You must check in before checking out.");
            }

            if (attendance.getCheckOut() != null) {
                return ResponseEntity.badRequest().body("⚠️ You have already checked out today.");
            }

            attendance.setCheckOut(time);
            attendance.setLatitude(req.getLatitude());
            attendance.setLongitude(req.getLongitude());
            attendance.setIpAddress(ipAddress);
            attendance.setUserAgent(userAgent);

            // ✅ Salary Calculation
            Duration duration = Duration.between(attendance.getCheckIn(), time);
            double totalHours = duration.toMinutes() / 60.0;

            // Get monthly salary of employee
            Employee emp = employeeRepository.findById(employeeId).orElse(null);
            if (emp != null && emp.getSalary() != null) {
                double hourlyRate = emp.getSalary() / 30 / 8; // Assuming 8hr work/day, 30 days/month
                double daySalary = Math.round(hourlyRate * totalHours * 100.0) / 100.0;
                attendance.setDaySalary(daySalary);
            }

            attendanceRepo.save(attendance);
            return ResponseEntity.ok("✅ Check-out successful!");
        }

        return ResponseEntity.badRequest().body("❌ Invalid attendance type.");
    }
    @GetMapping("/today")
    public ResponseEntity<List<TodayAttendanceDTO>> getTodayAttendance() {
        LocalDate today = LocalDate.now();

        List<Employee> allEmployees = employeeRepository.findAll();
        List<Attendance> todayEntries = attendanceRepo.findByDate(today);

        List<TodayAttendanceDTO> list = new ArrayList<>();

        for (Employee emp : allEmployees) {
            Attendance attendance = todayEntries.stream()
                    .filter(a -> a.getEmployeeId().equals(emp.getEmployeeId()))
                    .findFirst()
                    .orElse(null);

            String inTimeStr = attendance != null && attendance.getCheckIn() != null
                    ? attendance.getCheckIn().toString()
                    : null;

            String outTimeStr = attendance != null && attendance.getCheckOut() != null
                    ? attendance.getCheckOut().toString()
                    : null;

            double totalHours = 0;
            long breakMinutes = 0; // optional enhancement if you track breaks
            String status = "ABSENT";

            if (attendance != null && attendance.getCheckIn() != null && attendance.getCheckOut() != null) {
                Duration duration = Duration.between(attendance.getCheckIn(), attendance.getCheckOut());
                totalHours = Math.round((duration.toMinutes() / 60.0) * 100.0) / 100.0;

                // 🟢 Apply attendance policy
                if (totalHours >= 7.5) {
                    status = "PRESENT";
                } else if (totalHours >= 3.5) {
                    status = "LATE";
                } else {
                    status = "ABSENT";
                }
            }

            list.add(new TodayAttendanceDTO(
                    emp.getEmployeeId(),
                    emp.getName(),
                    inTimeStr,
                    outTimeStr,
                    breakMinutes,
                    totalHours,
                    status
            ));
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/monthly-salary")
    public ResponseEntity<Double> getMonthlySalary(Authentication auth) {
        String employeeId = auth.getName();
        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.of(now.getYear(), now.getMonth());
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        List<Attendance> monthlyRecords = attendanceRepo.findAll().stream()
                .filter(a -> a.getEmployeeId().equals(employeeId)
                        && !a.getDate().isBefore(start)
                        && !a.getDate().isAfter(now))
                .toList();

        double total = monthlyRecords.stream()
                .mapToDouble(a -> a.getDaySalary() != null ? a.getDaySalary() : 0.0)
                .sum();

        return ResponseEntity.ok(Math.round(total * 100.0) / 100.0);
    }

    @GetMapping("/employee/attendance/view")
    public ResponseEntity<List<AttendanceReportDTO>> viewAttendanceReport(Principal principal) {
        String employeeId = principal.getName();
        List<Attendance> records = attendanceRepo.findByEmployeeId(employeeId);


        Employee emp = employeeRepository.findById(employeeId).orElse(null);
        if (emp == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        double hourlyRate = emp.getSalary() / 26.0 / 8.0;

        List<AttendanceReportDTO> report = records.stream().map(a -> {
            String status = "ABSENT";
            double salary = 0;

            if (a.getCheckIn() != null && a.getCheckOut() != null) {
                long minutes = Duration.between(a.getCheckIn(), a.getCheckOut()).toMinutes();
                double hours = minutes / 60.0;

                if (hours >= 7.5) status = "PRESENT";
                else if (hours >= 3.5) status = "LATE";

                salary = Math.round(hours * hourlyRate * 100.0) / 100.0;
            }

            return new AttendanceReportDTO(a.getDate().toString(), status, salary);
        }).toList();

        return ResponseEntity.ok(report);
    }


}
