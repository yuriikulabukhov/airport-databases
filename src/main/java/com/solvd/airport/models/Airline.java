package com.solvd.airport.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Airline {
    private Long id;
    private String name;
    private java.math.BigDecimal safetyRating;
    private Integer fleetSize;
    private String contactEmail;
    private String maintenanceProvider;

    public Airline(Long id, String name, BigDecimal safetyRating, Integer fleetSize,
                   String contactEmail, String maintenanceProvider) {
        this.id = id;
        this.name = name;
        this.safetyRating = safetyRating;
        this.fleetSize = fleetSize;
        this.contactEmail = contactEmail;
        this.maintenanceProvider = maintenanceProvider;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getSafetyRating() { return safetyRating; }
    public void setSafetyRating(BigDecimal safetyRating) { this.safetyRating = safetyRating; }

    public Integer getFleetSize() { return fleetSize; }
    public void setFleetSize(Integer fleetSize) { this.fleetSize = fleetSize; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getMaintenanceProvider() { return maintenanceProvider; }
    public void setMaintenanceProvider(String maintenanceProvider) { this.maintenanceProvider = maintenanceProvider; }
}