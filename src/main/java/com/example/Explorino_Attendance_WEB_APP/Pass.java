package com.example.Explorino_Attendance_WEB_APP;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Pass
{
    public static void main(String[] args)
    {
        String pass=new BCryptPasswordEncoder(12).encode("HR002");
        System.out.println(pass);
    }
}
