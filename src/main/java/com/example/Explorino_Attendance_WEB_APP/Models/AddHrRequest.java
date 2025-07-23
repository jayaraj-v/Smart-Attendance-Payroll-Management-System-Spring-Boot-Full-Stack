package com.example.Explorino_Attendance_WEB_APP.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddHrRequest
{
    private String employeeId;
    private String password;
    private String name;
    private String dob;
    private String aadhaar;
    private String address;
    private String type;
    private String role; // Expecting "ROLE_HR"
    private String dateOfJoining;
    private Double salary;
    private String gender;
}
