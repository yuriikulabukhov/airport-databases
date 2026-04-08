package com.solvd.airport.dao;

import com.solvd.airport.models.AirportInfo;

import java.util.List;

public interface IAirportInfoDAO extends IBaseDAO<AirportInfo> {
    AirportInfo getByCode(String code);
    List<AirportInfo> getByCountry(String country);
}
