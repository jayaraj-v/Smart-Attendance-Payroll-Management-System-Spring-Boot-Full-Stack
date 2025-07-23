package com.example.Explorino_Attendance_WEB_APP.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leavebalance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBalance
{
    @Id
    private String employeeId;

    private int clAvailable;     // Casual Leave
    private int clUsed;

    private double plAvailable;  // Privilege Leave (can be half-day)
    private double plUsed;

    private int wlAvailable;     // Wellness Leave (1 per month)
    private int wlUsed;

    private int coAvailable;     // Compensatory Off
    private int coUsed;

    private int dlAvailable;     // Discretionary Leave
    private int dlUsed;

    private int mlAvailable;     // Maternity Leave (if applicable)
    private int mlUsed;

    private int patlAvailable;   // Paternity Leave
    private int patlUsed;
}
