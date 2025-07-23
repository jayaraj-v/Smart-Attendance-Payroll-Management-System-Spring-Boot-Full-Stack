package com.example.Explorino_Attendance_WEB_APP.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRequest
{
    private String type;     // IN or OUT
    private double latitude;
    private double longitude;
    private String date;     // yyyy-MM-dd
    private String time;
}
