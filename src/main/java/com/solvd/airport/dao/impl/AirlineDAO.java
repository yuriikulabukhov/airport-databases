package com.solvd.airport.dao.impl;

import com.solvd.airport.dao.IAirlineDAO;
import com.solvd.airport.models.Airline;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class AirlineDAO extends AbstractMySQLDB implements IAirlineDAO {

    private static final Logger logger = LoggerFactory.getLogger(AirlineDAO.class);

    @Override
    public Airline save(Airline entity) {
        String sql = "INSERT INTO Airlines (name, safety_rating, fleet_size, contact_email, maitenance_provider) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.setBigDecimal(2, entity.getSafetyRating());
            ps.setInt(3, entity.getFleetSize());
            ps.setString(4, entity.getContactEmail());
            ps.setString(5, entity.getMaintenanceProvider());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to save airline", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Airline getById(Long id) {
        String sql = "SELECT id, name, safety_rating, fleet_size, contact_email, maitenance_provider FROM Airlines WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Airline airline = new Airline();
                airline.setId(rs.getLong("id"));
                airline.setName(rs.getString("name"));
                airline.setSafetyRating(rs.getBigDecimal("safety_rating"));
                airline.setFleetSize(rs.getInt("fleet_size"));
                airline.setContactEmail(rs.getString("contact_email"));
                airline.setMaintenanceProvider(rs.getString("maitenance_provider"));
                return airline;
            }
        } catch (SQLException e) {
            logger.error("Failed to get airline by id {}", id, e);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return null;
    }

    @Override
    public void update(Airline entity) {}

    @Override
    public void deleteById(Long id) {}

    @Override
    public List<Airline> getAll() { return null; }

    @Override
    public Airline getByName(String name) { return null; }

    @Override
    public Airline getByContactEmail(String contactEmail) { return null; }

    @Override
    public List<Airline> getByMaintenanceProvider(String maintenanceProvider) { return null; }

    @Override
    public List<Airline> getWithSafetyRatingGreaterThan(BigDecimal minRating) { return null; }

    @Override
    public List<Airline> getWithFleetSizeGreaterThan(int minFleetSize) { return null; }
}
