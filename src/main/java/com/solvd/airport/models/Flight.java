package com.solvd.airport.models;

import com.solvd.airport.xml.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class Flight {
    private Long id;
    private String flightNumber;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime departureTime;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime arrivalTime;
    @XmlTransient
    private Long planesId;
    @XmlTransient
    private Long gatesId;
    private Gate gate;

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

    public Gate getGate() { return gate; }
    public void setGate(Gate gate) { this.gate = gate; }

    @Override
    public String toString() {
        return "Flight{id=" + id + ", flightNumber='" + flightNumber +
               "', departureTime=" + departureTime + ", arrivalTime=" + arrivalTime +
               ", gatesId=" + gatesId + "}";
    }
}

