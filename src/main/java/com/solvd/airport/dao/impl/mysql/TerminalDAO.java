package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.ITerminalDAO;
import com.solvd.airport.models.Terminal;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TerminalDAO extends AbstractMySQLDB implements ITerminalDAO {

    private static final Logger logger = LoggerFactory.getLogger(TerminalDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT id, terminal_name, capacity, check_in_count, luggage_belts, working_hours, Airport_info_id FROM Terminals";

    private Terminal mapRow(ResultSet rs) throws SQLException {
        Terminal terminal = new Terminal();
        terminal.setId(rs.getLong("id"));
        terminal.setTerminalName(rs.getString("terminal_name"));
        terminal.setCapacity(rs.getInt("capacity"));
        terminal.setCheckInCount(rs.getInt("check_in_count"));
        terminal.setLuggageBelts(rs.getInt("luggage_belts"));
        terminal.setWorkingHours(rs.getTime("working_hours").toLocalTime());
        terminal.setAirportInfoId(rs.getLong("Airport_info_id"));
        return terminal;
    }

    @Override
    public Terminal save(Terminal entity) {
        String sql = "INSERT INTO Terminals (terminal_name, capacity, check_in_count, luggage_belts, working_hours, Airport_info_id) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getTerminalName());
            ps.setInt(2, entity.getCapacity());
            ps.setInt(3, entity.getCheckInCount());
            ps.setInt(4, entity.getLuggageBelts());
            ps.setTime(5, Time.valueOf(entity.getWorkingHours()));
            ps.setLong(6, entity.getAirportInfoId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) entity.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Failed to save terminal", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<Terminal> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get terminal by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(Terminal entity) {
        String sql = "UPDATE Terminals SET terminal_name=?, capacity=?, check_in_count=?, luggage_belts=?, working_hours=?, Airport_info_id=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTerminalName());
            ps.setInt(2, entity.getCapacity());
            ps.setInt(3, entity.getCheckInCount());
            ps.setInt(4, entity.getLuggageBelts());
            ps.setTime(5, Time.valueOf(entity.getWorkingHours()));
            ps.setLong(6, entity.getAirportInfoId());
            ps.setLong(7, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update terminal with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Terminals WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete terminal with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Terminal> getAll() {
        List<Terminal> list = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get all terminals", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Terminal> getByAirportInfoId(Long airportInfoId) {
        String sql = SELECT_ALL_COLUMNS + " WHERE Airport_info_id = ?";
        List<Terminal> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, airportInfoId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get terminals by airport info id {}", airportInfoId, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public Optional<Terminal> getByName(String terminalName) {
        String sql = SELECT_ALL_COLUMNS + " WHERE terminal_name = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, terminalName);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get terminal by name {}", terminalName, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }
}
