package com.example.Explorino_Attendance_WEB_APP.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO
{
    private String employeeId;
    private String name;
    private String role;
    private String type;
    private int daysWorked;
    private int daysAbsent;
    private int performance;
}
