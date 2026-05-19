package com.solvd.airport.dao;

import com.solvd.airport.dao.impl.mysql.AirlineDAO;
import com.solvd.airport.models.Airline;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.math.BigDecimal;
import java.util.Optional;

public class AirlineDAOTest {

    private AirlineDAO airlineDAO;
    private Long savedId;

    @BeforeClass
    public void initDAO() {
        airlineDAO = new AirlineDAO();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (savedId != null) {
            airlineDAO.deleteById(savedId);
            savedId = null;
        }
    }

    @Test
    public void save_shouldCreateAirlineAndReturnGeneratedId() {
        Airline airline = new Airline();
        airline.setName("TestAirline");
        airline.setSafetyRating(new BigDecimal("4.5"));
        airline.setFleetSize(10);

        airlineDAO.save(airline);
        savedId = airline.getId();

        Assert.assertNotNull(airline.getId(), "save must insert id");
        Assert.assertTrue(airline.getId() > 0, "id must be positive");
    }

    @Test
    public void getById_shouldReturnAllFields() {
        Airline airline = new Airline();
        airline.setName("FieldAir");
        airline.setSafetyRating(new BigDecimal("3.8"));
        airline.setFleetSize(5);
        airline.setContactEmail("test@fieldair.com");
        airline.setMaintenanceProvider("Boeing");

        airlineDAO.save(airline);
        savedId = airline.getId();

        Optional<Airline> result = airlineDAO.getById(savedId);

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(result.isPresent(), "airline should be found by id");
        result.ifPresent(a -> {
            soft.assertEquals(a.getName(), "FieldAir");
            soft.assertEquals(a.getSafetyRating(), new BigDecimal("3.8"));
            soft.assertEquals(a.getFleetSize(), Integer.valueOf(5));
            soft.assertEquals(a.getContactEmail(), "test@fieldair.com");
            soft.assertEquals(a.getMaintenanceProvider(), "Boeing");
        });
        soft.assertAll();
    }

    @Test
    public void update_shouldModifyExistingAirline() {
        Airline airline = new Airline();
        airline.setName("Old");
        airline.setSafetyRating(new BigDecimal("4.0"));
        airline.setFleetSize(8);

        airlineDAO.save(airline);
        savedId = airline.getId();

        airline.setName("New");
        airlineDAO.update(airline);

        Optional<Airline> updated = airlineDAO.getById(savedId);
        Assert.assertTrue(updated.isPresent());
        Assert.assertEquals(updated.get().getName(), "New");
    }

    @Test
    public void deleteById_shouldRemoveAirline() {
        Airline airline = new Airline();
        airline.setName("DeleteAirline");
        airline.setSafetyRating(new BigDecimal("3.5"));
        airline.setFleetSize(3);

        airlineDAO.save(airline);
        savedId = airline.getId();

        airlineDAO.deleteById(savedId);
        savedId = null;

        Optional<Airline> result = airlineDAO.getById(airline.getId());
        Assert.assertFalse(result.isPresent(), "airline should not exist after delete");
    }
}
