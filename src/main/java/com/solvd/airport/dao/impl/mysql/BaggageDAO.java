package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.IBaggageDAO;
import com.solvd.airport.models.Baggage;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaggageDAO extends AbstractMySQLDB implements IBaggageDAO {

    private static final Logger logger = LoggerFactory.getLogger(BaggageDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT id, tag_number, checked_in_date, weight, Tickets_id FROM Baggage";

    private Baggage mapRow(ResultSet rs) throws SQLException {
        Baggage baggage = new Baggage();
        baggage.setId(rs.getLong("id"));
        baggage.setTagNumber(rs.getString("tag_number"));
        baggage.setCheckedInDate(rs.getTimestamp("checked_in_date").toLocalDateTime());
        baggage.setWeight(rs.getBigDecimal("weight"));
        baggage.setTicketsId(rs.getLong("Tickets_id"));
        return baggage;
    }

    @Override
    public Baggage save(Baggage entity) {
        String sql = "INSERT INTO Baggage (tag_number, checked_in_date, weight, Tickets_id) VALUES (?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getTagNumber());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getCheckedInDate()));
            ps.setBigDecimal(3, entity.getWeight());
            ps.setLong(4, entity.getTicketsId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) entity.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Failed to save baggage", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<Baggage> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get baggage by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(Baggage entity) {
        String sql = "UPDATE Baggage SET tag_number=?, checked_in_date=?, weight=?, Tickets_id=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTagNumber());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getCheckedInDate()));
            ps.setBigDecimal(3, entity.getWeight());
            ps.setLong(4, entity.getTicketsId());
            ps.setLong(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update baggage with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Baggage WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete baggage with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Baggage> getAll() {
        List<Baggage> list = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get all baggage", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public Optional<Baggage> getByTagNumber(String tagNumber) {
        String sql = SELECT_ALL_COLUMNS + " WHERE tag_number = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tagNumber);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get baggage by tag number {}", tagNumber, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<Baggage> getByTicketId(Long ticketsId) {
        String sql = SELECT_ALL_COLUMNS + " WHERE Tickets_id = ?";
        List<Baggage> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ticketsId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get baggage by ticket id {}", ticketsId, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }
}
