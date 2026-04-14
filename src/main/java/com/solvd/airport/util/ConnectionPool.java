package com.solvd.airport.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private static final int POOL_SIZE = 10;

    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static volatile ConnectionPool instance;
    private final BlockingQueue<Connection> pool;

    static {
        Properties props = new Properties();
        try (InputStream in = ConnectionPool.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties not found in classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }
        URL = props.getProperty("db.url");
        USERNAME = props.getProperty("db.username");
        PASSWORD = props.getProperty("db.password");
    }

    private ConnectionPool() {
        pool = new LinkedBlockingQueue<>(POOL_SIZE);
        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                pool.add(createConnection());
            }
            logger.info("Connection pool initialized with {} connections", POOL_SIZE);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public Connection getConnection() {
        try {
            Connection connection = pool.take();
            if (!connection.isValid(2)) {
                logger.warn("Stale connection detected, replacing");
                connection = createConnection();
            }
            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for connection", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to validate or replace connection", e);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            if (!pool.offer(connection)) {
                logger.warn("Connection pool is full, closing excess connection");
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Failed to close excess connection", e);
                }
            }
        }
    }
}
