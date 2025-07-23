package com.example.Explorino_Attendance_WEB_APP.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attendance")
public class Attendance
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;
    private String role;

    private LocalDate date;

    private LocalTime checkIn;
    private LocalTime checkOut;

    private Double daySalary = 0.0;

    private double latitude;
    private double longitude;

    private String ipAddress;
    private String userAgent;
}
