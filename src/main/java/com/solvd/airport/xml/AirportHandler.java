package com.solvd.airport.xml;

import com.solvd.airport.models.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AirportHandler extends DefaultHandler {

    private final List<Airline>  airlines  = new ArrayList<>();
    private final List<Plane>    planes    = new ArrayList<>();
    private final List<Flight>   flights   = new ArrayList<>();
    private final List<Gate>     gates     = new ArrayList<>();
    private final List<Terminal> terminals = new ArrayList<>();

    private Airline  currentAirline;
    private Plane    currentPlane;
    private Flight   currentFlight;
    private Gate     currentGate;
    private Terminal currentTerminal;

    private String        context = "";
    private StringBuilder data    = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        data = new StringBuilder();
        switch (qName) {
            case "airline"  -> { currentAirline  = new Airline();  context = "airline";  }
            case "plane"    -> { currentPlane    = new Plane();    context = "plane";    }
            case "flight"   -> { currentFlight   = new Flight();   context = "flight";   }
            case "gate"     -> { currentGate     = new Gate();     context = "gate";     }
            case "terminal" -> { currentTerminal = new Terminal(); context = "terminal"; }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        data.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String val = data.toString().trim();
        switch (qName) {

            case "airline"  -> { airlines.add(currentAirline);                                   context = "";        }
            case "plane"    -> { currentPlane.setAirlinesId(currentAirline.getId());
                                 planes.add(currentPlane);                                        context = "airline"; }
            case "flight"   -> { flights.add(currentFlight);                                     context = "airline"; }
            case "gate"     -> { currentFlight.setGatesId(currentGate.getId());
                                 gates.add(currentGate);                                          context = "flight";  }
            case "terminal" -> { currentGate.setTerminalsId(currentTerminal.getId());
                                 terminals.add(currentTerminal);                                  context = "gate";    }

            case "id" -> {
                long id = Long.parseLong(val);
                switch (context) {
                    case "airline"  -> currentAirline.setId(id);
                    case "plane"    -> currentPlane.setId(id);
                    case "flight"   -> currentFlight.setId(id);
                    case "gate"     -> currentGate.setId(id);
                    case "terminal" -> currentTerminal.setId(id);
                }
            }

            case "name"                -> { if ("airline".equals(context)) currentAirline.setName(val); }
            case "safetyRating"        -> currentAirline.setSafetyRating(new BigDecimal(val));
            case "fleetSize"           -> currentAirline.setFleetSize(Integer.parseInt(val));
            case "contactEmail"        -> currentAirline.setContactEmail(val);
            case "maintenanceProvider" -> currentAirline.setMaintenanceProvider(val);

            case "model"              -> currentPlane.setModel(val);
            case "boardNumber"        -> currentPlane.setBoardNumber(val);
            case "seatsCapacity"      -> currentPlane.setSeatsCapacity(Integer.parseInt(val));
            case "yearProduction"     -> currentPlane.setYearProduction(Integer.parseInt(val));

            case "flightNumber"      -> currentFlight.setFlightNumber(val);
            case "departureTime"     -> currentFlight.setDepartureTime(LocalDateTime.parse(val));
            case "arrivalTime"       -> currentFlight.setArrivalTime(LocalDateTime.parse(val));

            case "gateNumber"        -> currentGate.setGateNumber(val);
            case "floorLevel"        -> currentGate.setFloorLevel(Integer.parseInt(val));
            case "currentStatus"     -> currentGate.setCurrentStatus(val);
            case "boardingStartTime" -> currentGate.setBoardingStartTime(LocalDateTime.parse(val));

            case "terminalName"      -> currentTerminal.setTerminalName(val);
            case "capacity"          -> currentTerminal.setCapacity(Integer.parseInt(val));
            case "checkInCount"      -> currentTerminal.setCheckInCount(Integer.parseInt(val));
            case "luggageBelts"      -> currentTerminal.setLuggageBelts(Integer.parseInt(val));
            case "workingHours"      -> currentTerminal.setWorkingHours(LocalTime.parse(val));
        }
    }

    public List<Airline>  getAirlines()  { return airlines; }
    public List<Plane>    getPlanes()    { return planes; }
    public List<Flight>   getFlights()   { return flights; }
    public List<Gate>     getGates()     { return gates; }
    public List<Terminal> getTerminals() { return terminals; }
}
