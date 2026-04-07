package com.solvd.airport.models;

import java.time.LocalTime;

public class Terminal {
    private Long id;
    private String terminalName;
    private Integer capacity;
    private Integer checkInCount;
    private Integer luggageBelts;
    private java.time.LocalTime workingHours;
    private Long airportInfoId;

    public Terminal() {}

    public Terminal(Long id, String terminalName, Integer capacity, Integer checkInCount,
                    Integer luggageBelts, LocalTime workingHours, Long airportInfoId) {
        this.id = id;
        this.terminalName = terminalName;
        this.capacity = capacity;
        this.checkInCount = checkInCount;
        this.luggageBelts = luggageBelts;
        this.workingHours = workingHours;
        this.airportInfoId = airportInfoId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTerminalName() { return terminalName; }
    public void setTerminalName(String terminalName) { this.terminalName = terminalName;}

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getCheckInCount() { return checkInCount; }
    public void setCheckInCount(Integer checkInCount) { this.checkInCount = checkInCount;}

    public Integer getLuggageBelts() { return luggageBelts; }
    public void setLuggageBelts(Integer luggageBelts) { this.luggageBelts = luggageBelts; }

    public LocalTime getWorkingHours() { return workingHours; }
    public void setWorkingHours(LocalTime workingHours) { this.workingHours = workingHours; }

    public Long getAirportInfoId() { return airportInfoId; }
    public void setAirportInfoId(Long airportInfoId) { this.airportInfoId = airportInfoId; }

    @Override
    public String toString() {
        return "Terminal{" +
                "id=" + id +
                ", terminalName='" + terminalName + '\'' +
                ", capacity=" + capacity +
                ", checkInCount=" + checkInCount +
                ", luggageBelts=" + luggageBelts +
                ", workingHours=" + workingHours +
                ", airportInfoId=" + airportInfoId +
                '}';
    }
}
