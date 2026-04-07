package com.solvd.airport.models;

public class AirportInfo {
    private Long id;
    private String name;
    private String city;
    private String country;
    private String code;
    private String website;

    public AirportInfo() {}
    public AirportInfo(Long id, String name, String city, String country, String code, String website) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.code = code;
        this.website = website;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    @Override
    public String toString() {
        return "AirportInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", code='" + code + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}