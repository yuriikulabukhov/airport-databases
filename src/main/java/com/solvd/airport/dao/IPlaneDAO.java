package com.solvd.airport.dao;

import com.solvd.airport.models.Plane;

import java.util.List;
import java.util.Optional;

public interface IPlaneDAO extends IBaseDAO<Plane> {
    Optional<Plane> getByBoardNumber(String boardNumber);
    List<Plane> getByAirlineId(Long airlinesId);
}
