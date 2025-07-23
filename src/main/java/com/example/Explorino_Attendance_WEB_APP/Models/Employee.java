package com.example.Explorino_Attendance_WEB_APP.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee
{
    @Id
    private String employeeId;

    private String gender;
    private String name;
    private String dob;
    private String aadhaar;
    private String address;
    private String type;
    private String role;
    private String dateOfJoining;
    private Double salary;
}
