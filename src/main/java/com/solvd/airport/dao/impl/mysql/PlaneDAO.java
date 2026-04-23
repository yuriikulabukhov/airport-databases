package com.solvd.airport.dao.impl.mysql;

import com.solvd.airport.dao.IPlaneDAO;
import com.solvd.airport.models.Plane;
import com.solvd.airport.util.AbstractMySQLDB;
import com.solvd.airport.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaneDAO extends AbstractMySQLDB implements IPlaneDAO {

    private static final Logger logger = LoggerFactory.getLogger(PlaneDAO.class);
    private static final String SELECT_ALL_COLUMNS =
            "SELECT id, model, board_number, seats_capacity, year_production, Airlines_id FROM Planes";

    private Plane mapRow(ResultSet rs) throws SQLException {
        Plane plane = new Plane();
        plane.setId(rs.getLong("id"));
        plane.setModel(rs.getString("model"));
        plane.setBoardNumber(rs.getString("board_number"));
        plane.setSeatsCapacity(rs.getInt("seats_capacity"));
        plane.setYearProduction(rs.getInt("year_production"));
        plane.setAirlinesId(rs.getLong("Airlines_id"));
        return plane;
    }

    @Override
    public Plane save(Plane entity) {
        String sql = "INSERT INTO Planes (model, board_number, seats_capacity, year_production, Airlines_id) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getModel());
            ps.setString(2, entity.getBoardNumber());
            ps.setInt(3, entity.getSeatsCapacity());
            ps.setInt(4, entity.getYearProduction());
            ps.setLong(5, entity.getAirlinesId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) entity.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Failed to save plane", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return entity;
    }

    @Override
    public Optional<Plane> getById(Long id) {
        String sql = SELECT_ALL_COLUMNS + " WHERE id = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get plane by id {}", id, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void update(Plane entity) {
        String sql = "UPDATE Planes SET model=?, board_number=?, seats_capacity=?, year_production=?, Airlines_id=? WHERE id=?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getModel());
            ps.setString(2, entity.getBoardNumber());
            ps.setInt(3, entity.getSeatsCapacity());
            ps.setInt(4, entity.getYearProduction());
            ps.setLong(5, entity.getAirlinesId());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update plane with id {}", entity.getId(), e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Planes WHERE id = ?";
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete plane with id {}", id, e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<Plane> getAll() {
        List<Plane> list = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_COLUMNS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get all planes", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    @Override
    public Optional<Plane> getByBoardNumber(String boardNumber) {
        String sql = SELECT_ALL_COLUMNS + " WHERE board_number = ?";
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, boardNumber);
            rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get plane by board number {}", boardNumber, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<Plane> getByAirlineId(Long airlinesId) {
        String sql = SELECT_ALL_COLUMNS + " WHERE Airlines_id = ?";
        List<Plane> list = new ArrayList<>();
        ResultSet rs = null;
        Connection conn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, airlinesId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            logger.error("Failed to get planes by airline id {}", airlinesId, e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { logger.error("Failed to close ResultSet", e); }
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }
}
