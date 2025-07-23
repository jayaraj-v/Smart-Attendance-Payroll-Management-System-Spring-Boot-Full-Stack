package com.example.Explorino_Attendance_WEB_APP.Controllers;

import com.example.Explorino_Attendance_WEB_APP.Models.AddHrRequest;
import com.example.Explorino_Attendance_WEB_APP.Models.Employee;
import com.example.Explorino_Attendance_WEB_APP.Models.LeaveBalance;
import com.example.Explorino_Attendance_WEB_APP.Models.Users;
import com.example.Explorino_Attendance_WEB_APP.Repository.EmployeeRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.LeaveBalanceRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private UsersRepository usersRepo;

    @Autowired
    private LeaveBalanceRepository leaveRepo;

    @PostMapping("/add-hr")
    public ResponseEntity<String> addHr(@RequestBody AddHrRequest req)
    {
        // Save to employee table
        Employee emp = new Employee(
                req.getEmployeeId(),
                req.getName(),
                req.getGender(), // 👈 Include gender here
                req.getDob(),
                req.getAadhaar(),
                req.getAddress(),
                req.getType(),
                "HR",
                req.getDateOfJoining(),
                req.getSalary()
        );

        employeeRepo.save(emp);
        String rawPassword = req.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        Users user = new Users(req.getEmployeeId(), encodedPassword, req.getRole().replace("ROLE_",""));
        usersRepo.save(user);

        LeaveBalance lb = new LeaveBalance();
        lb.setEmployeeId(emp.getEmployeeId());
        leaveRepo.save(lb);
        return ResponseEntity.ok("HR Added Successfully");
    }
}
