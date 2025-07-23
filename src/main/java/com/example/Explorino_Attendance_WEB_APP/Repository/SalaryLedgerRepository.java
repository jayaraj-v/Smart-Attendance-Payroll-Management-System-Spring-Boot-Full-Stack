package com.example.Explorino_Attendance_WEB_APP.Repository;

import com.example.Explorino_Attendance_WEB_APP.Models.Attendance;
import com.example.Explorino_Attendance_WEB_APP.Models.SalaryLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalaryLedgerRepository extends JpaRepository<SalaryLedger,Long>
{
    boolean existsByEmployeeIdAndMonth(String employeeId, String month);
//    List<Attendance> findByEmployeeIdAndDateBetween(String employeeId, LocalDate start, LocalDate end);
}
