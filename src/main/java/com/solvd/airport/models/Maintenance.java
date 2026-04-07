package com.solvd.airport.models;

import java.time.LocalDate;

public class Maintenance {
    private Long id;
    private String checkType;
    private java.time.LocalDate lastServiceDate;
    private java.time.LocalDate nextServiceDate;
    private Long staffId;
    private Long planesId;
    public Maintenance() {}

    public Maintenance(Long id, String checkType, LocalDate lastServiceDate,
                       LocalDate nextServiceDate, Long staffId, Long planesId) {
        this.id = id;
        this.checkType = checkType;
        this.lastServiceDate = lastServiceDate;
        this.nextServiceDate = nextServiceDate;
        this.staffId = staffId;
        this.planesId = planesId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id;}

    public String getCheckType() { return checkType; }
    public void setCheckType(String checkType) { this.checkType = checkType; }

    public LocalDate getLastServiceDate() { return lastServiceDate; }
    public void setLastServiceDate(LocalDate lastServiceDate) { this.lastServiceDate = lastServiceDate; }

    public LocalDate getNextServiceDate() { return nextServiceDate; }
    public void setNextServiceDate(LocalDate nextServiceDate) { this.nextServiceDate = nextServiceDate; }

    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }

    public Long getPlanesId() { return planesId; }
    public void setPlanesId(Long planesId) { this.planesId = planesId; }

    @Override
    public String toString() {
        return "Maintenance{" +
                "id=" + id +
                ", checkType='" + checkType + '\'' +
                ", lastServiceDate=" + lastServiceDate +
                ", nextServiceDate=" + nextServiceDate +
                ", staffId=" + staffId +
                ", planesId=" + planesId +
                '}';
    }
}
