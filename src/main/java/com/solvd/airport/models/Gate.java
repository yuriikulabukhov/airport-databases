package com.solvd.airport.models;

import com.solvd.airport.xml.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class Gate {
    private Long id;
    private String gateNumber;
    private Integer floorLevel;
    private String currentStatus;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private java.time.LocalDateTime boardingStartTime;
    @XmlTransient
    private Long terminalsId;
    private Terminal terminal;

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

    public Terminal getTerminal() { return terminal; }
    public void setTerminal(Terminal terminal) { this.terminal = terminal; }

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