package com.solvd.airport.dao;

import com.solvd.airport.models.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface IFlightDAO extends IBaseDAO<Flight> {
    Flight getByFlightNumber(String flightNumber);
    List<Flight> getByDepartureTimeBetween(LocalDateTime from, LocalDateTime to);
    List<Flight> getByPlaneId(Long planesId);
    List<Flight> getByGateId(Long gatesId);
}
