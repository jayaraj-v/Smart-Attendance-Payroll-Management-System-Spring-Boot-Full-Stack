package com.example.Explorino_Attendance_WEB_APP.Configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler
{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException
    {
        String authority = authentication.getAuthorities().iterator().next().getAuthority();
        String role = authority.replace("ROLE_", ""); // ✅ Normalize role

        switch (role) {
            case "ADMIN":
                response.sendRedirect("/admin-dashboard.html");
                break;
            case "HR":
                response.sendRedirect("/hr-dashboard.html");
                break;
            case "EMPLOYEE":
                response.sendRedirect("/employee-dashboard.html");
                break;
            default:
                response.sendRedirect("/login?error=true");
        }
    }
}
