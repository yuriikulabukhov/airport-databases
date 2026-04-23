package com.solvd.airport.dao;

import com.solvd.airport.models.AirportInfo;

import java.util.List;
import java.util.Optional;

public interface IAirportInfoDAO extends IBaseDAO<AirportInfo> {
    Optional<AirportInfo> getByCode(String code);
    List<AirportInfo> getByCountry(String country);
}
