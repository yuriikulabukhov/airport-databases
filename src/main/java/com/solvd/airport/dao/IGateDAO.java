package com.solvd.airport.dao;

import com.solvd.airport.models.Gate;

import java.util.List;

public interface IGateDAO extends IBaseDAO<Gate> {
    List<Gate> getByStatus(String status);
    List<Gate> getByTerminalId(Long terminalsId);
}
