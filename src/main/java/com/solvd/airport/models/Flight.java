package com.solvd.airport.models;

import java.time.LocalDateTime;

public class Flight {
    private Long id;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Long planesId;
    private Long gatesId;

    public Flight() {}

    public Flight(Long id, String flightNumber, LocalDateTime departureTime, LocalDateTime arrivalTime, Long planesId, Long gatesId) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.planesId = planesId;
        this.gatesId = gatesId;
    }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime;}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public Long getPlanesId() { return planesId; }
    public void setPlanesId(Long planesId) { this.planesId = planesId; }

    public Long getGatesId() { return gatesId; }
    public void setGatesId(Long gatesId) { this.gatesId = gatesId;}

    @Override
    public String toString() {
        return "Flight{id=" + id + ", flightNumber='" + flightNumber +
               "', departureTime=" + departureTime + ", arrivalTime=" + arrivalTime +
               ", gatesId=" + gatesId + "}";
    }
}

