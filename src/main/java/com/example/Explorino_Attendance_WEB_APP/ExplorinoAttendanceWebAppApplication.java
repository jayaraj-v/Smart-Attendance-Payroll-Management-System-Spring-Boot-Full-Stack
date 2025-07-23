package com.example.Explorino_Attendance_WEB_APP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class ExplorinoAttendanceWebAppApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(ExplorinoAttendanceWebAppApplication.class, args);
	}
}
