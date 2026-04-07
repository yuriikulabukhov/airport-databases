package com.solvd.airport.models;

import java.math.BigDecimal;

public class Ticket {
    private Long id;
    private String seatNumber;
    private java.math.BigDecimal price;
    private String bookingStatus;
    private Long passengersId;
    public Ticket() {}

    public Ticket(Long id, String seatNumber, BigDecimal price,
                  String bookingStatus, Long passengersId) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.price = price;
        this.bookingStatus = bookingStatus;
        this.passengersId = passengersId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public Long getPassengersId() { return passengersId; }
    public void setPassengersId(Long passengersId) { this.passengersId = passengersId; }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", seatNumber='" + seatNumber + '\'' +
                ", price=" + price +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", passengersId=" + passengersId +
                '}';
    }
}
