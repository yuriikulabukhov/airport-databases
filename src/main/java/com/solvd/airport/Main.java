package com.solvd.airport;

import com.solvd.airport.models.Airline;
import com.solvd.airport.models.Flight;
import com.solvd.airport.models.Plane;
import com.solvd.airport.xml.XMLParserSAX;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        XMLParserSAX.main(args);

        logger.info(" JAXB ");
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("airport.xml")) {
            JAXBContext ctx = JAXBContext.newInstance(Airline.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            Airline airline = (Airline) unmarshaller.unmarshal(is);

            logger.info(" Airline ");
            logger.info("{}", airline);
            logger.info(" Planes ");
            for (Plane p : airline.getPlanes()) logger.info("{}", p);
            logger.info(" Flights ");
            for (Flight f : airline.getFlights()) {
                logger.info("{}", f);
                logger.info(" gate: {} ", f.getGate());
                logger.info(" terminal: {} ", f.getGate().getTerminal());
            }
        } catch (JAXBException | IOException e) {
            logger.error("Failed to parse airport.xml", e);
        }
    }
}
