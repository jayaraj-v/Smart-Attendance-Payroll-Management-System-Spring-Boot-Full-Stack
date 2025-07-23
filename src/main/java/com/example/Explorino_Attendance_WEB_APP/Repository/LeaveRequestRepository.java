package com.example.Explorino_Attendance_WEB_APP.Repository;

import com.example.Explorino_Attendance_WEB_APP.Models.LeaveBalance;
import com.example.Explorino_Attendance_WEB_APP.Models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long>
{
    List<LeaveRequest> findByStatus(String status);
}
