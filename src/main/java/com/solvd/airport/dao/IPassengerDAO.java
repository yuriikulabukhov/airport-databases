package com.solvd.airport.dao;

import com.solvd.airport.models.Passenger;
import java.util.List;

public interface IPassengerDAO extends IBaseDAO<Passenger> {
    List<Passenger> getByNationality(String nationality);
    List<Passenger> getTopFlyersByMiles(int limit);
}
