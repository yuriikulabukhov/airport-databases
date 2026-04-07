package com.solvd.airport.models;

public class Plane {
    private Long id;
    private String model;
    private String boardNumber;
    private Integer seatsCapacity;
    private Integer yearProduction;
    private Long airlinesId;
    public Plane() {}
    public Plane(Long id, String model, String boardNumber, Integer seatsCapacity,
                 Integer yearProduction, Long airlinesId) {
        this.id = id;
        this.model = model;
        this.boardNumber = boardNumber;
        this.seatsCapacity = seatsCapacity;
        this.yearProduction = yearProduction;
        this.airlinesId = airlinesId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getBoardNumber() { return boardNumber; }
    public void setBoardNumber(String boardNumber) { this.boardNumber = boardNumber; }

    public Integer getSeatsCapacity() { return seatsCapacity; }
    public void setSeatsCapacity(Integer seatsCapacity) { this.seatsCapacity = seatsCapacity; }

    public Integer getYearProduction() { return yearProduction; }
    public void setYearProduction(Integer yearProduction) { this.yearProduction = yearProduction; }

    public Long getAirlinesId() { return airlinesId; }
    public void setAirlinesId(Long airlinesId) { this.airlinesId = airlinesId; }

    @Override
    public String toString() {
        return "Plane{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", boardNumber='" + boardNumber + '\'' +
                ", seatsCapacity=" + seatsCapacity +
                ", yearProduction=" + yearProduction +
                ", airlinesId=" + airlinesId +
                '}';
    }
}
