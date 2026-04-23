package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.ITicketDAO;
import com.solvd.airport.models.Ticket;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDAO extends AbstractMySQLDB implements ITicketDAO {

    private static final Logger logger = LoggerFactory.getLogger(TicketDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT id, seat_number, price, booking_status, Passengers_id FROM Tickets";

    private Ticket mapRow(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getLong("id"));
        ticket.setSeatNumber(rs.getString("seat_number"));
        ticket.setPrice(rs.getBigDecimal("price"));
        ticket.setBookingStatus(rs.getString("booking_status"));
        ticket.setPassengersId(rs.getLong("Passengers_id"));
        return ticket;
    }

    @Override
    public Ticket save(Ticket entity) {
        String sql = "INSERT INTO Tickets (seat_number, price, booking_status, Passengers_id) VALUES (?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getSeatNumber());
            ps.setBigDecimal(2, entity.getPrice());
            ps.setString(3, entity.getBookingStatus());
            ps.setLong(4, entity.getPassengersId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) entity.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Failed to save ticket", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<Ticket> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get ticket by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(Ticket entity) {
        String sql = "UPDATE Tickets SET seat_number=?, price=?, booking_status=?, Passengers_id=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getSeatNumber());
            ps.setBigDecimal(2, entity.getPrice());
            ps.setString(3, entity.getBookingStatus());
            ps.setLong(4, entity.getPassengersId());
            ps.setLong(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update ticket with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Tickets WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete ticket with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Ticket> getAll() {
        List<Ticket> list = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get all tickets", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Ticket> getByBookingStatus(String bookingStatus) {
        String sql = SELECT_ALL_COLUMNS + " WHERE booking_status = ?";
        List<Ticket> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookingStatus);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get tickets by booking status {}", bookingStatus, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Ticket> getByPassengerId(Long passengersId) {
        String sql = SELECT_ALL_COLUMNS + " WHERE Passengers_id = ?";
        List<Ticket> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, passengersId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get tickets by passenger id {}", passengersId, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }
}
