package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.IFlightDAO;
import com.solvd.airport.models.Flight;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDAO extends AbstractMySQLDB implements IFlightDAO {

    private static final Logger logger = LoggerFactory.getLogger(FlightDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT id, flight_number, departure_time, arrival_time, Planes_id, Gates_id FROM Flights";

    private Flight mapRow(ResultSet rs) throws SQLException {
        Flight flight = new Flight();
        flight.setId(rs.getLong("id"));
        flight.setFlightNumber(rs.getString("flight_number"));
        flight.setDepartureTime(rs.getTimestamp("departure_time").toLocalDateTime());
        flight.setArrivalTime(rs.getTimestamp("arrival_time").toLocalDateTime());
        flight.setPlanesId(rs.getLong("Planes_id"));
        flight.setGatesId(rs.getLong("Gates_id"));
        return flight;
    }

    @Override
    public Flight save(Flight entity) {
        String sql = "INSERT INTO Flights (flight_number, departure_time, arrival_time, Planes_id, Gates_id) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getFlightNumber());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getDepartureTime()));
            ps.setTimestamp(3, Timestamp.valueOf(entity.getArrivalTime()));
            ps.setLong(4, entity.getPlanesId());
            ps.setLong(5, entity.getGatesId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) entity.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Failed to save flight", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<Flight> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get flight by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(Flight entity) {
        String sql = "UPDATE Flights SET flight_number=?, departure_time=?, arrival_time=?, Planes_id=?, Gates_id=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getFlightNumber());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getDepartureTime()));
            ps.setTimestamp(3, Timestamp.valueOf(entity.getArrivalTime()));
            ps.setLong(4, entity.getPlanesId());
            ps.setLong(5, entity.getGatesId());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update flight with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Flights WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete flight with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Flight> getAll() {
        List<Flight> list = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get all flights", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public Optional<Flight> getByFlightNumber(String flightNumber) {
        String sql = SELECT_ALL_COLUMNS + " WHERE flight_number = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flightNumber);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get flight by flight number {}", flightNumber, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<Flight> getByDepartureTimeBetween(LocalDateTime from, LocalDateTime to) {
        String sql = SELECT_ALL_COLUMNS + " WHERE departure_time BETWEEN ? AND ?";
        List<Flight> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(from));
            ps.setTimestamp(2, Timestamp.valueOf(to));
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get flights between {} and {}", from, to, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Flight> getByPlaneId(Long planesId) {
        String sql = SELECT_ALL_COLUMNS + " WHERE Planes_id = ?";
        List<Flight> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, planesId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get flights by plane id {}", planesId, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public List<Flight> getByGateId(Long gatesId) {
        String sql = SELECT_ALL_COLUMNS + " WHERE Gates_id = ?";
        List<Flight> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, gatesId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get flights by gate id {}", gatesId, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }
}
