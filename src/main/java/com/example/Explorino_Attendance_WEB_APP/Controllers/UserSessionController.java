package com.example.Explorino_Attendance_WEB_APP.Controllers;

import com.example.Explorino_Attendance_WEB_APP.Models.Employee;
import com.example.Explorino_Attendance_WEB_APP.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserSessionController
{
    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping("/session-info")
    public ResponseEntity<Map<String, String>> getSessionUser(Principal principal) {
        String employeeId = principal.getName();
        Optional<Employee> empOpt = employeeRepo.findById(employeeId);

        if (empOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Employee emp = empOpt.get();
        Map<String, String> response = new HashMap<>();
        response.put("employeeId", emp.getEmployeeId());
        response.put("name", emp.getName());
        response.put("role", emp.getType());  // You can also return `emp.getRole()` if needed

        return ResponseEntity.ok(response);
    }
}
