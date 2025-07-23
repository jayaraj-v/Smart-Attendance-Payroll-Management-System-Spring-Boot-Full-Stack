package com.example.Explorino_Attendance_WEB_APP.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/attendance/today").hasAnyRole("HR", "ADMIN")
                        .requestMatchers("/login.html", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/hr/employees/all").hasAnyRole("HR", "ADMIN") // 👈 Add ADMIN here
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html") // 👈 NOT /login
                        .loginProcessingUrl("/login") // this remains the same
                        .successHandler(customLoginSuccessHandler())
                        .permitAll()
                )
                .logout(logout->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, CustomAuthenticationProvider provider) throws Exception
    {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(provider)
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler()
    {
        return new CustomLoginSuccessHandler();
    }
}
