package com.solvd.airport.dao;

import com.solvd.airport.models.Baggage;

import java.util.List;

public interface IBaggageDAO extends IBaseDAO<Baggage> {
    Baggage getByTagNumber(String tagNumber);
    List<Baggage> getByTicketId(Long ticketsId);
}
