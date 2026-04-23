package com.solvd.airport.dao;

import com.solvd.airport.models.Airline;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IAirlineDAO extends IBaseDAO<Airline> {
    Optional<Airline> getByName(String name);
    Optional<Airline> getByContactEmail(String contactEmail);
    List<Airline> getByMaintenanceProvider(String maintenanceProvider);
    List<Airline> getWithSafetyRatingGreaterThan(BigDecimal minRating);
    List<Airline> getWithFleetSizeGreaterThan(int minFleetSize);
}
