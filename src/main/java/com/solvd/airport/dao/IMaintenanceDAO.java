package com.solvd.airport.dao;

import com.solvd.airport.models.Maintenance;

import java.time.LocalDate;
import java.util.List;

public interface IMaintenanceDAO extends IBaseDAO<Maintenance> {
    List<Maintenance> getByCheckType(String checkType);
    List<Maintenance> getByPlaneId(Long planesId);
    List<Maintenance> getByStaffId(Long staffId);
    List<Maintenance> getUpcomingBefore(LocalDate date);
}
