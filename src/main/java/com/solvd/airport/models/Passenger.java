package com.solvd.airport.models;

import java.time.LocalDate;

public class Passenger extends User {
    private String nationality;
    private String passengerType;
    private Integer bonusMiles;
    private LocalDate membershipDate;

    public Passenger() {}

    public Passenger(Long id, String firstName, String lastName, LocalDate dateOfBirth, String email,
                     String phoneNumber, String nationality, String passengerType,
                     Integer bonusMiles, LocalDate membershipDate) {
        super(id, firstName, lastName, dateOfBirth, email, phoneNumber);
        this.nationality = nationality;
        this.passengerType = passengerType;
        this.bonusMiles = bonusMiles;
        this.membershipDate = membershipDate;
    }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getPassengerType() { return passengerType; }
    public void setPassengerType(String passengerType) { this.passengerType = passengerType; }

    public Integer getBonusMiles() { return bonusMiles; }
    public void setBonusMiles(Integer bonusMiles) { this.bonusMiles = bonusMiles; }

    public LocalDate getMembershipDate() { return membershipDate; }
    public void setMembershipDate(LocalDate membershipDate) { this.membershipDate = membershipDate; }
}

