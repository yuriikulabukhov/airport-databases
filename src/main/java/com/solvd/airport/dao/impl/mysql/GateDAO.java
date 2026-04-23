package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.IGateDAO;
import com.solvd.airport.models.Gate;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GateDAO extends AbstractMySQLDB implements IGateDAO {

    private static final Logger logger = LoggerFactory.getLogger(GateDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT id, gate_number, floor_level, current_status, boarding_start_time, Terminals_id FROM Gates";

    private Gate mapRow(ResultSet rs) throws SQLException {
        Gate gate = new Gate();
        gate.setId(rs.getLong("id"));
        gate.setGateNumber(rs.getString("gate_number"));
        gate.setFloorLevel(rs.getInt("floor_level"));
        gate.setCurrentStatus(rs.getString("current_status"));
        gate.setBoardingStartTime(rs.getTimestamp("boarding_start_time").toLocalDateTime());
        gate.setTerminalsId(rs.getLong("Terminals_id"));
        return gate;
    }

    @Override
    public Gate save(Gate entity) {
        String sql = "INSERT INTO Gates (gate_number, floor_level, current_status, boarding_start_time, Terminals_id) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getGateNumber());
            ps.setInt(2, entity.getFloorLevel());
            ps.setString(3, entity.getCurrentStatus());
            ps.setTimestamp(4, Timestamp.valueOf(entity.getBoardingStartTime()));
            ps.setLong(5, entity.getTerminalsId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) entity.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Failed to save gate", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<Gate> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get gate by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(Gate entity) {
        String sql = "UPDATE Gates SET gate_number=?, floor_level=?, current_status=?, boarding_start_time=?, Terminals_id=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getGateNumber());
            ps.setInt(2, entity.getFloorLevel());
            ps.setString(3, entity.getCurrentStatus());
            ps.setTimestamp(4, Timestamp.valueOf(entity.getBoardingStartTime()));
            ps.setLong(5, entity.getTerminalsId());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update gate with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Gates WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete gate with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Gate> getAll() {
        List<Gate> list = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get all gates", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Gate> getByStatus(String status) {
        String sql = SELECT_ALL_COLUMNS + " WHERE current_status = ?";
        List<Gate> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get gates by status {}", status, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Gate> getByTerminalId(Long terminalsId) {
        String sql = SELECT_ALL_COLUMNS + " WHERE Terminals_id = ?";
        List<Gate> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, terminalsId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get gates by terminal id {}", terminalsId, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }
}
