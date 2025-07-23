package com.example.Explorino_Attendance_WEB_APP.Controllers;

import com.example.Explorino_Attendance_WEB_APP.Models.Users;
import com.example.Explorino_Attendance_WEB_APP.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class PasswordController
{
    @Autowired
    private UsersRepository usersRepo;

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String,String> req, Principal principal)
    {
        String username=principal.getName();
        String currentPassword=req.get("currentPassword");
        String newPassword=req.get("newPassword");

        Users user=usersRepo.findById(username).orElse(null);

        if(user==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found");

        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        if(!encoder.matches(currentPassword,user.getPassword())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Current password is incorrect");

        user.setPassword(encoder.encode(newPassword));
        usersRepo.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping("/user/role")
    public ResponseEntity<String> getUserRole(Principal principal)
    {
        String username=principal.getName();
        Users user=usersRepo.findById(username).orElse(null);

        if(user==null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unknown");
        return ResponseEntity.ok(user.getRole().toUpperCase());
    }
}
