package com.solvd.airport.models;

import java.time.LocalDate;
import java.math.BigDecimal;

public class Staff extends User {
    private LocalDate hireDate;
    private String role;
    private BigDecimal salary;

    public Staff() {}

    public Staff(Long id, String firstName, String lastName, LocalDate dateOfBirth, String email,
                 String phoneNumber, LocalDate hireDate, String role, BigDecimal salary) {
        super(id, firstName, lastName, dateOfBirth, email, phoneNumber);
        this.hireDate = hireDate;
        this.role = role;
        this.salary = salary;
    }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
}
