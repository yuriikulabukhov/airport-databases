package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.IAirlineDAO;
import com.solvd.airport.models.Airline;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirlineDAO extends AbstractMySQLDB implements IAirlineDAO {

    private static final Logger logger = LoggerFactory.getLogger(AirlineDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT id, name, safety_rating, fleet_size, contact_email, maitenance_provider FROM Airlines";

    private Airline mapRow(ResultSet rs) throws SQLException {
        Airline airline = new Airline();
        airline.setId(rs.getLong("id"));
        airline.setName(rs.getString("name"));
        airline.setSafetyRating(rs.getBigDecimal("safety_rating"));
        airline.setFleetSize(rs.getInt("fleet_size"));
        airline.setContactEmail(rs.getString("contact_email"));
        airline.setMaintenanceProvider(rs.getString("maitenance_provider"));
        return airline;
    }

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
    public Optional<Airline> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get airline by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

@Override
    public void update(Airline entity) {
        String sql = "UPDATE Airlines SET name=?, safety_rating=?, fleet_size=?, contact_email=?, maitenance_provider=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setBigDecimal(2, entity.getSafetyRating());
            ps.setInt(3, entity.getFleetSize());
            ps.setString(4, entity.getContactEmail());
            ps.setString(5, entity.getMaintenanceProvider());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update airline with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Airlines WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete airline with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Airline> getAll() {
        List<Airline> airlines = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                airlines.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Failed to get all airlines", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return airlines;
    }

    @Override
    public Optional<Airline> getByName(String name) {
        String sql = SELECT_ALL_COLUMNS + " WHERE name = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get airline by name {}", name, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Airline> getByContactEmail(String contactEmail) {
        String sql = SELECT_ALL_COLUMNS + " WHERE contact_email = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contactEmail);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get airline by contact email {}", contactEmail, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<Airline> getByMaintenanceProvider(String maintenanceProvider) {
        String sql = SELECT_ALL_COLUMNS + " WHERE maitenance_provider = ?";
        List<Airline> airlines = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maintenanceProvider);
            rs = ps.executeQuery();
            while (rs.next()) airlines.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get airlines by maintenance provider {}", maintenanceProvider, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return airlines;
    }

    @Override
    public List<Airline> getWithSafetyRatingGreaterThan(BigDecimal minRating) {
        String sql = SELECT_ALL_COLUMNS + " WHERE safety_rating > ?";
        List<Airline> airlines = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, minRating);
            rs = ps.executeQuery();
            while (rs.next()) airlines.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get airlines with safety rating > {}", minRating, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return airlines;
    }

    @Override
    public List<Airline> getWithFleetSizeGreaterThan(int minFleetSize) {
        String sql = SELECT_ALL_COLUMNS + " WHERE fleet_size > ?";
        List<Airline> airlines = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minFleetSize);
            rs = ps.executeQuery();
            while (rs.next()) airlines.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get airlines with fleet size > {}", minFleetSize, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return airlines;
    }
}
