package com.solvd.airport.dao;

import com.solvd.airport.models.Plane;

import java.util.List;

public interface IPlaneDAO extends IBaseDAO<Plane> {
    Plane getByBoardNumber(String boardNumber);
    List<Plane> getByAirlineId(Long airlinesId);
}
