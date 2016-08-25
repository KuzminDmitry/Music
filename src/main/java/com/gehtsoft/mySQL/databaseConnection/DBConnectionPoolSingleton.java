package com.gehtsoft.mySQL.databaseConnection;


import com.gehtsoft.configProperties.ConfigProperties;
import com.gehtsoft.date.IDateService;
import com.gehtsoft.factory.DateFactory;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by dkuzmin on 7/28/2016.
 */
public class DBConnectionPoolSingleton {

    final static Logger logger = Logger.getLogger("resource");

    private static volatile DBConnectionPoolSingleton instance;

    private List<DBConnection> dbConnections = new ArrayList<>();

    private static Integer maxAmount;
    private static Integer resetTimeInSeconds;
    private static Integer amountOfLocked;

    private IDateService dateService = DateFactory.getDateService();

    private DBConnectionPoolSingleton() throws SQLException {
        logger.info("Started.");
        Properties properties = ConfigProperties.getProperties();
        Integer minAmount = Integer.parseInt(properties.getProperty("database.pool.connection.minAmount"));
        maxAmount = Integer.parseInt(properties.getProperty("database.pool.connection.maxAmount"));
        resetTimeInSeconds = Integer.parseInt(properties.getProperty("database.connection.lifetime.second"));
        logger.info("Properties: " + "minAmount=" + minAmount + "; " + "maxAmount=" + maxAmount + "; " + "resetTimeInSeconds=" + resetTimeInSeconds);
        amountOfLocked = 0;
        dbConnections = new ArrayList<>();
        for (int i = 0; i < minAmount; i++) {
            this.dbConnections.add(new DBConnection());
        }
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                resetLocks();
            }
        }, resetTimeInSeconds * 1000, resetTimeInSeconds * 1000);
    }

    public static DBConnectionPoolSingleton getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DBConnectionPoolSingleton.class) {
                if (instance == null) {
                    instance = new DBConnectionPoolSingleton();
                }
            }
        }
        return instance;
    }

    public synchronized DBConnection getDBConnection() throws SQLException {
        for (DBConnection dbConnection : dbConnections) {
            if (!dbConnection.isLocked()) {
                amountOfLocked++;
                dbConnection.setLocked(true);
                dbConnection.setResetTime(dateService.getNow());
                if(logger.isDebugEnabled()) {
                    logger.debug("Connection locked: " + dbConnection.getConnection());
                    logger.debug("Amount of unlocked connections: " + (dbConnections.size() - amountOfLocked));
                    logger.debug("Amount of locked connections: " + amountOfLocked);
                }
                return dbConnection;
            }
        }
        if (dbConnections.size() != maxAmount) {
            amountOfLocked++;
            DBConnection dbConnection = new DBConnection();
            dbConnection.setLocked(true);
            dbConnections.add(dbConnection);
            dbConnection.setResetTime(dateService.getDate(Calendar.SECOND, resetTimeInSeconds));
            if(logger.isDebugEnabled()) {
                logger.debug("Connection locked: " + dbConnection.getConnection());
                logger.debug("Amount of unlocked connections: " + (dbConnections.size() - amountOfLocked));
                logger.debug("Amount of locked connections: " + amountOfLocked);
            }
            return dbConnection;
        }
        return null;
    }

    public synchronized void retrieveDBConnection(DBConnection dbConnection) throws SQLException {
        for (DBConnection dbc : dbConnections) {
            if (dbc.equals(dbConnection)) {
                amountOfLocked--;
                dbc.setLocked(false);
                dbConnection.setResetTime(dateService.getDate(Calendar.SECOND, resetTimeInSeconds));
                if(logger.isDebugEnabled()) {
                    logger.debug("Connection unlocked: " + dbConnection.getConnection());
                    logger.debug("Amount of unlocked connections: " + (dbConnections.size() - amountOfLocked));
                    logger.debug("Amount of locked connections: " + amountOfLocked);
                }
                return;
            }
        }
    }

    private synchronized void resetLocks() {
        boolean cleared = false;
        if(logger.isDebugEnabled()) {
            logger.debug("Start clearing locked connection with timeout.");
        }
        for (DBConnection dbConnection : dbConnections) {
            Date now = dateService.getNow();
            if (dbConnection.isLocked() && (dbConnection.getResetTime().before(now)) && (now.after(dbConnection.getResetTime()))) {
                if(logger.isDebugEnabled()) {
                    logger.debug("Connection removed back to unlocked: " + dbConnection.getConnection());
                }
                dbConnection.setLocked(false);
                cleared = true;
            }
        }
        if (!cleared) {
            if(logger.isDebugEnabled()) {
                logger.debug("There are no locked connection for clear!");
            }
        }
    }
}
