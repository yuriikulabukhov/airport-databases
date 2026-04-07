package com.solvd.airport.models;

import java.time.LocalDateTime;

public class Gate {
    private Long id;
    private String gateNumber;
    private Integer floorLevel;
    private String currentStatus;
    private java.time.LocalDateTime boardingStartTime;
    private Long terminalsId;
    public Gate() {}

    public Gate(Long id, String gateNumber, Integer floorLevel, String currentStatus,
                LocalDateTime boardingStartTime, Long terminalsId) {
        this.id = id;
        this.gateNumber = gateNumber;
        this.floorLevel = floorLevel;
        this.currentStatus = currentStatus;
        this.boardingStartTime = boardingStartTime;
        this.terminalsId = terminalsId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGateNumber() { return gateNumber; }
    public void setGateNumber(String gateNumber) { this.gateNumber = gateNumber; }

    public Integer getFloorLevel() { return floorLevel; }
    public void setFloorLevel(Integer floorLevel) { this.floorLevel = floorLevel; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

    public LocalDateTime getBoardingStartTime() { return boardingStartTime; }
    public void setBoardingStartTime(LocalDateTime boardingStartTime) { this.boardingStartTime = boardingStartTime; }

    public Long getTerminalsId() { return terminalsId; }
    public void setTerminalsId(Long terminalsId) { this.terminalsId = terminalsId; }

    @Override
    public String toString() {
        return "Gate{" +
                "id=" + id +
                ", gateNumber='" + gateNumber + '\'' +
                ", floorLevel=" + floorLevel +
                ", currentStatus='" + currentStatus + '\'' +
                ", boardingStartTime=" + boardingStartTime +
                ", terminalsId=" + terminalsId +
                '}';
    }
}