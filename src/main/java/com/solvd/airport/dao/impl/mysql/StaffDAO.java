package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.IStaffDAO;
import com.solvd.airport.models.Staff;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffDAO extends AbstractMySQLDB implements IStaffDAO {

    private static final Logger logger = LoggerFactory.getLogger(StaffDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT s.id AS s_id, u.first_name, u.last_name, u.date_of_birth, u.email, u.phone_number, " +
            "s.hire_date, s.role, s.salary " +
            "FROM Staff s JOIN Users u ON s.User_id = u.id";

    private Staff mapRow(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getLong("s_id"));
        staff.setFirstName(rs.getString("first_name"));
        staff.setLastName(rs.getString("last_name"));
        staff.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        staff.setEmail(rs.getString("email"));
        staff.setPhoneNumber(rs.getString("phone_number"));
        staff.setHireDate(rs.getDate("hire_date").toLocalDate());
        staff.setRole(rs.getString("role"));
        staff.setSalary(rs.getBigDecimal("salary"));
        return staff;
    }

    @Override
    public Staff save(Staff entity) {
        String userSql = "INSERT INTO Users (first_name, last_name, date_of_birth, email, phone_number) VALUES (?, ?, ?, ?, ?)";
        String staffSql = "INSERT INTO Staff (User_id, hire_date, role, salary) VALUES (?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try {
            long userId;
            try (PreparedStatement ps = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getFirstName());
                ps.setString(2, entity.getLastName());
                ps.setDate(3, Date.valueOf(entity.getDateOfBirth()));
                ps.setString(4, entity.getEmail());
                ps.setString(5, entity.getPhoneNumber());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Failed to get generated User id");
                    userId = keys.getLong(1);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(staffSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, userId);
                ps.setDate(2, Date.valueOf(entity.getHireDate()));
                ps.setString(3, entity.getRole());
                ps.setBigDecimal(4, entity.getSalary());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) entity.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to save staff", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<Staff> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE s.id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get staff by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(Staff entity) {
        String updateUserSql = "UPDATE Users u JOIN Staff s ON u.id = s.User_id " +
                "SET u.first_name=?, u.last_name=?, u.date_of_birth=?, u.email=?, u.phone_number=? WHERE s.id=?";
        String updateStaffSql = "UPDATE Staff SET hire_date=?, role=?, salary=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps1 = conn.prepareStatement(updateUserSql);
             PreparedStatement ps2 = conn.prepareStatement(updateStaffSql)) {
            ps1.setString(1, entity.getFirstName());
            ps1.setString(2, entity.getLastName());
            ps1.setDate(3, Date.valueOf(entity.getDateOfBirth()));
            ps1.setString(4, entity.getEmail());
            ps1.setString(5, entity.getPhoneNumber());
            ps1.setLong(6, entity.getId());
            ps1.executeUpdate();
            ps2.setDate(1, Date.valueOf(entity.getHireDate()));
            ps2.setString(2, entity.getRole());
            ps2.setBigDecimal(3, entity.getSalary());
            ps2.setLong(4, entity.getId());
            ps2.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update staff with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String getUserIdSql = "SELECT User_id FROM Staff WHERE id = ?";
        String deleteStaffSql = "DELETE FROM Staff WHERE id = ?";
        String deleteUserSql = "DELETE FROM Users WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try {
            Long userId = null;
            try (PreparedStatement ps = conn.prepareStatement(getUserIdSql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) userId = rs.getLong("User_id");
                }
            }
            if (userId == null) { logger.warn("Staff with id {} not found", id); return; }
            try (PreparedStatement ps = conn.prepareStatement(deleteStaffSql)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(deleteUserSql)) {
                ps.setLong(1, userId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Failed to delete staff with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Staff> getAll() {
        List<Staff> list = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get all staff", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Staff> getByRole(String role) {
        String sql = SELECT_ALL_COLUMNS + " WHERE s.role = ?";
        List<Staff> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get staff by role {}", role, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Staff> getWithSalaryGreaterThan(BigDecimal minSalary) {
        String sql = SELECT_ALL_COLUMNS + " WHERE s.salary > ?";
        List<Staff> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, minSalary);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get staff with salary > {}", minSalary, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }
}
