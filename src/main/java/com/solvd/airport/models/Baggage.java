package com.solvd.airport.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Baggage {
    private Long id;
    private String tagNumber;
    private java.time.LocalDateTime checkedInDate;
    private java.math.BigDecimal weight;
    private Long ticketsId;
    public Baggage() {}
    public Baggage(Long id, String tagNumber, LocalDateTime checkedInDate,
                   BigDecimal weight, Long ticketsId) {
        this.id = id;
        this.tagNumber = tagNumber;
        this.checkedInDate = checkedInDate;
        this.weight = weight;
        this.ticketsId = ticketsId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id;}

    public String getTagNumber() { return tagNumber; }
    public void setTagNumber(String tagNumber) { this.tagNumber = tagNumber; }

    public LocalDateTime getCheckedInDate() { return checkedInDate; }
    public void setCheckedInDate(LocalDateTime checkedInDate) { this.checkedInDate = checkedInDate; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public Long getTicketsId() { return ticketsId; }
    public void setTicketsId(Long ticketsId) { this.ticketsId = ticketsId; }

    @Override
    public String toString() {
        return "Baggage{" +
                "id=" + id +
                ", tagNumber='" + tagNumber + '\'' +
                ", checkedInDate=" + checkedInDate +
                ", weight=" + weight +
                ", ticketsId=" + ticketsId +
                '}';
    }
}