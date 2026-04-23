package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.IAirportInfoDAO;
import com.solvd.airport.models.AirportInfo;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirportInfoDAO extends AbstractMySQLDB implements IAirportInfoDAO {

    private static final Logger logger = LoggerFactory.getLogger(AirportInfoDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT id, name, city, country, code, website FROM Airports_info";

    private AirportInfo mapRow(ResultSet rs) throws SQLException {
        AirportInfo airportInfo = new AirportInfo();
        airportInfo.setId(rs.getLong("id"));
        airportInfo.setName(rs.getString("name"));
        airportInfo.setCity(rs.getString("city"));
        airportInfo.setCountry(rs.getString("country"));
        airportInfo.setCode(rs.getString("code"));
        airportInfo.setWebsite(rs.getString("website"));
        return airportInfo;
    }

    @Override
    public AirportInfo save(AirportInfo entity) {
        String sql = "INSERT INTO Airports_info (name, city, country, code, website) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getCity());
            ps.setString(3, entity.getCountry());
            ps.setString(4, entity.getCode());
            ps.setString(5, entity.getWebsite());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to save airport info", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<AirportInfo> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get airport info by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(AirportInfo entity) {
        String sql = "UPDATE Airports_info SET name=?, city=?, country=?, code=?, website=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getCity());
            ps.setString(3, entity.getCountry());
            ps.setString(4, entity.getCode());
            ps.setString(5, entity.getWebsite());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update airport info with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Airports_info WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete airport info with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<AirportInfo> getAll() {
        List<AirportInfo> airports = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                airports.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Failed to get all airports info", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return airports;
    }

    @Override
    public Optional<AirportInfo> getByCode(String code) {
        String sql = SELECT_ALL_COLUMNS + " WHERE code = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get airport info by code {}", code, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<AirportInfo> getByCountry(String country) {
        String sql = SELECT_ALL_COLUMNS + " WHERE country = ?";
        List<AirportInfo> airports = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, country);
            rs = ps.executeQuery();
            while (rs.next()) airports.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get airports info by country {}", country, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return airports;
    }
}
