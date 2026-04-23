package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.IMaintenanceDAO;
import com.solvd.airport.models.Maintenance;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaintenanceDAO extends AbstractMySQLDB implements IMaintenanceDAO {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT id, check_type, last_service_date, next_service_date, Staff_id, Planes_id FROM Maintenance";

    private Maintenance mapRow(ResultSet rs) throws SQLException {
        Maintenance m = new Maintenance();
        m.setId(rs.getLong("id"));
        m.setCheckType(rs.getString("check_type"));
        m.setLastServiceDate(rs.getDate("last_service_date").toLocalDate());
        m.setNextServiceDate(rs.getDate("next_service_date").toLocalDate());
        m.setStaffId(rs.getLong("Staff_id"));
        m.setPlanesId(rs.getLong("Planes_id"));
        return m;
    }

    @Override
    public Maintenance save(Maintenance entity) {
        String sql = "INSERT INTO Maintenance (check_type, last_service_date, next_service_date, Staff_id, Planes_id) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getCheckType());
            ps.setDate(2, Date.valueOf(entity.getLastServiceDate()));
            ps.setDate(3, Date.valueOf(entity.getNextServiceDate()));
            ps.setLong(4, entity.getStaffId());
            ps.setLong(5, entity.getPlanesId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) entity.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Failed to save maintenance record", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<Maintenance> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get maintenance by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(Maintenance entity) {
        String sql = "UPDATE Maintenance SET check_type=?, last_service_date=?, next_service_date=?, Staff_id=?, Planes_id=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCheckType());
            ps.setDate(2, Date.valueOf(entity.getLastServiceDate()));
            ps.setDate(3, Date.valueOf(entity.getNextServiceDate()));
            ps.setLong(4, entity.getStaffId());
            ps.setLong(5, entity.getPlanesId());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update maintenance with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Maintenance WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete maintenance with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Maintenance> getAll() {
        List<Maintenance> list = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get all maintenance records", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Maintenance> getByCheckType(String checkType) {
        String sql = SELECT_ALL_COLUMNS + " WHERE check_type = ?";
        List<Maintenance> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, checkType);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get maintenance by check type {}", checkType, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Maintenance> getByPlaneId(Long planesId) {
        String sql = SELECT_ALL_COLUMNS + " WHERE Planes_id = ?";
        List<Maintenance> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, planesId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get maintenance by plane id {}", planesId, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Maintenance> getByStaffId(Long staffId) {
        String sql = SELECT_ALL_COLUMNS + " WHERE Staff_id = ?";
        List<Maintenance> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, staffId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get maintenance by staff id {}", staffId, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Maintenance> getUpcomingBefore(LocalDate date) {
        String sql = SELECT_ALL_COLUMNS + " WHERE next_service_date < ?";
        List<Maintenance> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get upcoming maintenance before {}", date, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }
}
