package com.solvd.airport.dao;

import com.solvd.airport.models.Terminal;

import java.util.List;
import java.util.Optional;

public interface ITerminalDAO extends IBaseDAO<Terminal> {
    List<Terminal> getByAirportInfoId(Long airportInfoId);
    Optional<Terminal> getByName(String terminalName);
}
