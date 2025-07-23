package com.example.Explorino_Attendance_WEB_APP.Repository;

import com.example.Explorino_Attendance_WEB_APP.Models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long>
{
    Optional<Attendance> findByEmployeeIdAndDate(String employeeId, LocalDate date);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByEmployeeId(String employeeId);
    List<Attendance> findByEmployeeIdAndDateBetween(String employeeId, LocalDate start, LocalDate end);

}
