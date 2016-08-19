package com.gehtsoft.mySQL.databaseConnection;


import com.gehtsoft.configProperties.ConfigProperties;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by dkuzmin on 7/6/2016.
 */
public class DBConnection {

    final static Logger logger = Logger.getLogger("databaseConnection");

    public Connection getConnection() {
        return connection;
    }

    private Connection connection = null;

    private boolean locked = false;

    private Date resetTime = new Date();

    public DBConnection() throws SQLException {
        Properties properties = ConfigProperties.getProperties();
        String driver = properties.getProperty("database.driver");
        String url = properties.getProperty("database.url");
        String characterEncoding = properties.getProperty("database.characterEncoding");
        String user = properties.getProperty("database.user");
        String password = properties.getProperty("database.password");

        properties = new Properties();
        properties.setProperty("characterEncoding", characterEncoding);
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("useUnicode", "true");
        logger.info("DBConnection created with properties: " + properties);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.error("Class driver not found!");
            if(logger.isDebugEnabled()){
                logger.debug(e.getStackTrace());
            }
            throw new SQLException(e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            logger.error("Get connection failed!");
            if(logger.isDebugEnabled()){
                logger.debug(e.getStackTrace());
            }
            throw new SQLException(e.getMessage());
        }
    }

    public void commit() throws SQLException {
        if (locked) {
            connection.commit();
            setAutoCommit(true);
        } else {
            logger.error("Operation not allowed after connection returned to pool!");
            throw new SQLException("Operation not allowed after connection returned to pool!");
        }
    }

    public void setAutoCommit(boolean value) throws SQLException {
        if (locked) {
            connection.setAutoCommit(value);
        } else {
            logger.error("Operation not allowed after connection returned to pool!");
            throw new SQLException("Operation not allowed after connection returned to pool!");
        }
    }


    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        if (locked) {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        } else {
            logger.error("Operation not allowed after connection returned to pool!");
            throw new SQLException("Operation not allowed after connection returned to pool!");
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (locked) {
            return connection.prepareStatement(sql);
        } else {
            logger.error("Operation not allowed after connection returned to pool!");
            throw new SQLException("Operation not allowed after connection returned to pool!");
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Date getResetTime() {
        return resetTime;
    }

    public void setResetTime(Date resetTime) {
        this.resetTime = resetTime;
    }
}

