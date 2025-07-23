package com.example.Explorino_Attendance_WEB_APP.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayAttendanceDTO
{
    private String employeeId;
    private String name;
    private String inTime;
    private String outTime;
    private long breakMinutes;
    private double totalHours;
    private String status;
}
