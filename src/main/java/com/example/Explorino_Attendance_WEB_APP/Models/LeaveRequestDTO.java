package com.example.Explorino_Attendance_WEB_APP.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestDTO
{
    private Long leaveId;
    private String employeeId;
    private String employeeName;
    private String leaveType;
    private String reason;
    private int days;
    private LocalDate submissionDate;
}
