# 🕒Smart-Attendance-Payroll-Management-System-Spring-Boot-Full-Stack
An advanced and fully-featured Attendance &amp; Payroll Management System built using Spring Boot for backend and HTML/CSS/JS for frontend. This project is designed for real-time workforce tracking, salary processing, and HR operations — fully aligned with Explorinno's internal HR policies.

## 📌 Project Overview

The **Attendance & Payroll Management System** is a full-featured **Spring Boot-based web application** developed to manage employee attendance, salary processing, and leave management with full **role-based access**. It automates payroll calculations using punch-in/out times, ensures compliance with HR policies, and enables HR/Admin to view detailed reports and manage leave workflows.

---

## ✨ Features

### 🔐 Role-Based Login
- Login as `Employee` to check-in/out and view attendance.
- Login as `HR` to manage employees, leaves, and reports.
- Login as `Admin` to oversee and manage the entire system.

### 🕘 Attendance Management
- Punch-in and Punch-out with timestamp and geolocation.
- View attendance status and total working hours.
- Automatic salary calculation based on hours worked.

### 💸 Payroll Processing
- Calculates day-wise salary from monthly salary using working hours.
- Tracks and updates employee `daySalary` for each working day.
- View daily/monthly payroll reports.

### 📝 Leave Management (HR)
- Apply and approve CL, PL, WL, DL, and CO leaves.
- Leave encashment and balance tracking.
- Compliant with Explorinno Leave & Attendance Policies.

### 📊 Reporting Dashboard
- HR/Admin views complete employee reports.
- Attendance, salary, and leave summaries by date or user.

---

## 📂 Data Handling

- Stores employee, attendance, and leave data in **MySQL database**.
- Authenticates users via Spring Security.
- Validates logic based on company policies.

---

## 🏗️ How It Works

### 1️⃣ Login Page
- Accepts role, username, and password.
- Redirects to respective dashboards upon authentication.

### 2️⃣ Employee Dashboard
- Punch In/Out attendance
- Track attendance and working hours
- View salary earned for each day

### 3️⃣ HR Dashboard
- Add/edit employee details
- View attendance and approve leaves
- Generate salary and attendance reports

### 4️⃣ Admin Panel
- Access full control over system data
- Monitor HR and employee activities

---

## 🛠 Tech Stack

- **Backend**: Spring Boot, Spring Security, Spring Data JPA
- **Frontend**: HTML5, CSS3, JavaScript
- **Database**: MySQL
- **Tools**: IntelliJ IDEA, Postman, Git

---


📈 Future Enhancements
      📄 PDF export of salary slips and attendance logs
      📧 Email alerts for approvals and attendance logs
      📱 Mobile responsiveness & PWA support
      📊 Visual charts using Recharts or Chart.js
      🌐 Deployment on cloud (Render, AWS, Railway)

🤝 Contributing
      Pull requests are welcome! For major changes, open an issue first to discuss what you'd like to improve or add.

📜 License
      This project is licensed under the MIT License – see the LICENSE file for details.

📧 Contact
    JAYARAJ V
📩 jayaraj.veluchamy@gmail.com
🌐 https://github.com/jayaraj-v


