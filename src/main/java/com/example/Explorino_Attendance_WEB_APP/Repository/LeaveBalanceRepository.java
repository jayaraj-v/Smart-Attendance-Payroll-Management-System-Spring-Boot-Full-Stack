package com.example.Explorino_Attendance_WEB_APP.Repository;

import com.example.Explorino_Attendance_WEB_APP.Models.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance,String>
{
}
