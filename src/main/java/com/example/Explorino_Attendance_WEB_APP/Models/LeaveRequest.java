package com.example.Explorino_Attendance_WEB_APP.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;

    @Column(nullable = false)
    private String employeeId;

    @Column(nullable = false)
    private String leaveType; // CL, PL, WL, DL, CO, MPL

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private int days;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
}
