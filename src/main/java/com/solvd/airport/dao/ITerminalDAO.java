package com.solvd.airport.dao;

import com.solvd.airport.models.Terminal;

import java.util.List;

public interface ITerminalDAO extends IBaseDAO<Terminal> {
    List<Terminal> getByAirportInfoId(Long airportInfoId);
    Terminal getByName(String terminalName);
}
