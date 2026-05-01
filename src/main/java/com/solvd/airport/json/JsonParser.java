package com.solvd.airport.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.solvd.airport.models.Airline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonParser {
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);
    private static final ObjectMapper MAPPER = buildMapper();

    private static ObjectMapper buildMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    public static void serialize(Airline airline, OutputStream out) throws IOException {
        MAPPER.writeValue(out, airline);
        logger.info("Airline serialized to JSON: {}", airline.getName());
    }

    public static Airline deserialize(InputStream in) throws IOException {
        Airline airline = MAPPER.readValue(in, Airline.class);
        logger.info("Airline deserialized from JSON: {}", airline.getName());
        return airline;
    }
}
