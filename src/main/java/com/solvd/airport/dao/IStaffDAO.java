package com.solvd.airport.dao;

import com.solvd.airport.models.Staff;
import java.util.List;

public interface IStaffDAO extends IBaseDAO<Staff> {
    List<Staff> getByRole(String role);
    List<Staff> getWithSalaryGreaterThan(java.math.BigDecimal minSalary);
}
