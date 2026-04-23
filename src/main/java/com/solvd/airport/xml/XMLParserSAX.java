package com.solvd.airport.xml;

import com.solvd.airport.models.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XMLParserSAX {

    private static final Logger logger = LoggerFactory.getLogger(XMLParserSAX.class);

    public static void main(String[] args) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try (InputStream is = XMLParserSAX.class.getClassLoader().getResourceAsStream("airport.xml")) {
            AirportHandler handler = new AirportHandler();
            factory.newSAXParser().parse(is, handler);

            List<Airline>  airlines  = handler.getAirlines();
            List<Plane>    planes    = handler.getPlanes();
            List<Flight>   flights   = handler.getFlights();
            List<Gate>     gates     = handler.getGates();
            List<Terminal> terminals = handler.getTerminals();

            logger.info(" Airlines ");
            for (Airline a : airlines)   logger.info("{}", a);
            logger.info(" Planes ");
            for (Plane p : planes)       logger.info("{}", p);
            logger.info(" Flights ");
            for (Flight f : flights)     logger.info("{}", f);
            logger.info(" Gates ");
            for (Gate g : gates)         logger.info("{}", g);
            logger.info(" Terminals ");
            for (Terminal t : terminals) logger.info("{}", t);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error("Failed to parse airport.xml", e);
        }
    }
}
