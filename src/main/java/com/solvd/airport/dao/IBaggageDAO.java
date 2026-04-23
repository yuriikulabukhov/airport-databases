package com.solvd.airport.dao;

import com.solvd.airport.models.Baggage;

import java.util.List;
import java.util.Optional;

public interface IBaggageDAO extends IBaseDAO<Baggage> {
    Optional<Baggage> getByTagNumber(String tagNumber);
    List<Baggage> getByTicketId(Long ticketsId);
}
