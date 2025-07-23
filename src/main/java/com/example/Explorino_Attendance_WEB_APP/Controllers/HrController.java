package com.example.Explorino_Attendance_WEB_APP.Controllers;

import com.example.Explorino_Attendance_WEB_APP.Models.*;
import com.example.Explorino_Attendance_WEB_APP.Repository.EmployeeRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.LeaveBalanceRepository;
import com.example.Explorino_Attendance_WEB_APP.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hr")
public class HrController
{
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private LeaveBalanceRepository leaveRepo;

    @PostMapping("/add-employee")
    public ResponseEntity<String> addEmployee(@RequestBody AddHrRequest req)
    {
        Employee emp = new Employee(
                req.getEmployeeId(),
                req.getName(),
                req.getGender(),
                req.getDob(),
                req.getAadhaar(),
                req.getAddress(),
                req.getType(),
                "EMPLOYEE",
                req.getDateOfJoining(),
                req.getSalary()
        );

        LeaveBalance lb = new LeaveBalance();
        lb.setEmployeeId(emp.getEmployeeId());
        leaveRepo.save(lb);

        employeeRepository.save(emp);
        String password= new BCryptPasswordEncoder().encode(req.getPassword());

        usersRepository.save(new Users(
                req.getEmployeeId(),password,req.getRole().replace("ROLE_","")
        ));
        return ResponseEntity.ok("New Employee added👍");

    }

    @GetMapping("/employees/all")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees()
    {
        List<Employee> allEmployees=employeeRepository.findAll();
        List<EmployeeDTO> empDtoList=allEmployees.stream().map(
                emp->{
                    EmployeeDTO dto=new EmployeeDTO();
                    dto.setEmployeeId(emp.getEmployeeId());
                    dto.setName(emp.getName());
                    dto.setRole(emp.getRole());
                    dto.setType(emp.getType());

                    dto.setDaysWorked(0);
                    dto.setDaysAbsent(0);
                    dto.setPerformance(0);
                    return dto;
                }
        ).toList();
        return ResponseEntity.ok(empDtoList);
    }

}
