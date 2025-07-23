package com.example.Explorino_Attendance_WEB_APP.Repository;

import com.example.Explorino_Attendance_WEB_APP.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,String>
{
    Optional<Users> findByUsernameAndRole(String username, String role);
}
