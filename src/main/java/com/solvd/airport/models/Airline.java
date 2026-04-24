package com.solvd.airport.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;
import java.util.List;

@XmlRootElement(name = "airline")
@XmlAccessorType(XmlAccessType.FIELD)
public class Airline {
    private Long id;
    private String name;
    private java.math.BigDecimal safetyRating;
    private Integer fleetSize;
    private String contactEmail;
    private String maintenanceProvider;
    @XmlElementWrapper(name = "planes")
    @XmlElement(name = "plane")
    private List<Plane> planes;
    @XmlElementWrapper(name = "flights")
    @XmlElement(name = "flight")
    private List<Flight> flights;

    public Airline() {}

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

    public List<Plane> getPlanes() { return planes; }
    public void setPlanes(List<Plane> planes) { this.planes = planes; }

    public List<Flight> getFlights() { return flights; }
    public void setFlights(List<Flight> flights) { this.flights = flights; }

    @Override
    public String toString() {
        return "Airline{id=" + id + ", name='" + name + "', safetyRating=" + safetyRating +
               ", fleetSize=" + fleetSize + ", contactEmail='" + contactEmail +
               "', maintenanceProvider='" + maintenanceProvider + "'}";
    }
}