package com.solvd.airport.dao.impl;

import com.solvd.airport.dao.IAirportInfoDAO;
import com.solvd.airport.models.AirportInfo;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class AirportInfoDAO extends AbstractMySQLDB implements IAirportInfoDAO {

    private static final Logger logger = LoggerFactory.getLogger(AirportInfoDAO.class);

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
    public AirportInfo getById(Long id) {
        String sql = "SELECT id, name, city, country, code, website FROM Airports_info WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                AirportInfo airportInfo = new AirportInfo();
                airportInfo.setId(rs.getLong("id"));
                airportInfo.setName(rs.getString("name"));
                airportInfo.setCity(rs.getString("city"));
                airportInfo.setCountry(rs.getString("country"));
                airportInfo.setCode(rs.getString("code"));
                airportInfo.setWebsite(rs.getString("website"));
                return airportInfo;
            }
        } catch (SQLException e) {
            logger.error("Failed to get airport info by id {}", id, e);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return null;
    }

    @Override
    public void update(AirportInfo entity) {}

    @Override
    public void deleteById(Long id) {}

    @Override
    public List<AirportInfo> getAll() { return null; }

    @Override
    public AirportInfo getByCode(String code) { return null; }

    @Override
    public List<AirportInfo> getByCountry(String country) { return null; }
}
