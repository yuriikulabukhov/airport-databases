package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.IPassengerDAO;
import com.solvd.airport.models.Passenger;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PassengerDAO extends AbstractMySQLDB implements IPassengerDAO {

    private static final Logger logger = LoggerFactory.getLogger(PassengerDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT p.id AS p_id, u.first_name, u.last_name, u.date_of_birth, u.email, u.phone_number, " +
            "p.nationality, p.passenger_type, p.bonus_miles, p.membership_date " +
            "FROM Passengers p JOIN Users u ON p.User_id = u.id";

    private Passenger mapRow(ResultSet rs) throws SQLException {
        Passenger passenger = new Passenger();
        passenger.setId(rs.getLong("p_id"));
        passenger.setFirstName(rs.getString("first_name"));
        passenger.setLastName(rs.getString("last_name"));
        passenger.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        passenger.setEmail(rs.getString("email"));
        passenger.setPhoneNumber(rs.getString("phone_number"));
        passenger.setNationality(rs.getString("nationality"));
        passenger.setPassengerType(rs.getString("passenger_type"));
        passenger.setBonusMiles(rs.getInt("bonus_miles"));
        Date membershipDate = rs.getDate("membership_date");
        if (membershipDate != null) passenger.setMembershipDate(membershipDate.toLocalDate());
        return passenger;
    }

    @Override
    public Passenger save(Passenger entity) {
        String userSql = "INSERT INTO Users (first_name, last_name, date_of_birth, email, phone_number) VALUES (?, ?, ?, ?, ?)";
        String passengerSql = "INSERT INTO Passengers (User_id, nationality, passenger_type, bonus_miles, membership_date) VALUES (?, ?, ?, ?, ?)";
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
            try (PreparedStatement ps = conn.prepareStatement(passengerSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, userId);
                ps.setString(2, entity.getNationality());
                ps.setString(3, entity.getPassengerType());
                ps.setInt(4, entity.getBonusMiles());
                ps.setDate(5, entity.getMembershipDate() != null ? Date.valueOf(entity.getMembershipDate()) : null);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) entity.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to save passenger", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<Passenger> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE p.id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get passenger by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(Passenger entity) {
        String updateUserSql = "UPDATE Users u JOIN Passengers p ON u.id = p.User_id " +
                "SET u.first_name=?, u.last_name=?, u.date_of_birth=?, u.email=?, u.phone_number=? WHERE p.id=?";
        String updatePassengerSql = "UPDATE Passengers SET nationality=?, passenger_type=?, bonus_miles=?, membership_date=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps1 = conn.prepareStatement(updateUserSql);
             PreparedStatement ps2 = conn.prepareStatement(updatePassengerSql)) {
            ps1.setString(1, entity.getFirstName());
            ps1.setString(2, entity.getLastName());
            ps1.setDate(3, Date.valueOf(entity.getDateOfBirth()));
            ps1.setString(4, entity.getEmail());
            ps1.setString(5, entity.getPhoneNumber());
            ps1.setLong(6, entity.getId());
            ps1.executeUpdate();
            ps2.setString(1, entity.getNationality());
            ps2.setString(2, entity.getPassengerType());
            ps2.setInt(3, entity.getBonusMiles());
            ps2.setDate(4, entity.getMembershipDate() != null ? Date.valueOf(entity.getMembershipDate()) : null);
            ps2.setLong(5, entity.getId());
            ps2.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update passenger with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String getUserIdSql = "SELECT User_id FROM Passengers WHERE id = ?";
        String deletePassengerSql = "DELETE FROM Passengers WHERE id = ?";
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
            if (userId == null) { logger.warn("Passenger with id {} not found", id); return; }
            try (PreparedStatement ps = conn.prepareStatement(deletePassengerSql)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(deleteUserSql)) {
                ps.setLong(1, userId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Failed to delete passenger with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Passenger> getAll() {
        List<Passenger> list = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get all passengers", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Passenger> getByNationality(String nationality) {
        String sql = SELECT_ALL_COLUMNS + " WHERE p.nationality = ?";
        List<Passenger> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nationality);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get passengers by nationality {}", nationality, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Passenger> getTopFlyersByMiles(int limit) {
        String sql = SELECT_ALL_COLUMNS + " ORDER BY p.bonus_miles DESC LIMIT ?";
        List<Passenger> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get top flyers by miles {}", limit, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
        }

}
