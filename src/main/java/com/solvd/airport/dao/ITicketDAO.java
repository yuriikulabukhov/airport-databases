package com.solvd.airport.dao;

import com.solvd.airport.models.Ticket;

import java.util.List;

public interface ITicketDAO extends IBaseDAO<Ticket> {
    List<Ticket> getByBookingStatus(String bookingStatus);
    List<Ticket> getByPassengerId(Long passengersId);
}
