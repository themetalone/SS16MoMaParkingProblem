package com.github.themetalone.parking.core.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by steff on 12.07.2016.
 */
public class SQLiteSimulationDataCollectorImpl implements SimulationDataCollector {

    private Logger LOG = LoggerFactory.getLogger(SQLiteSimulationDataCollectorImpl.class);
    private Connection connection = null;
    private Statement batchStatement = null;
    private int batchSize = 0;
    private int batchThreshold = 1000;

    public SQLiteSimulationDataCollectorImpl(String location) {
        String jdbcPrefix = "jdbc:sqlite";
        String jdbcUrl = jdbcPrefix + ":" + location;
        try {
            String jdbcDriver = "org.sqlite.JDBC";
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl);
            Statement stmnt = connection.createStatement();
            String sqlMakeCarTable = "CREATE TABLE IF NOT EXISTS cars (" +
                    "id NUMERIC," +
                    "parkingSlotId NUMERIC," +
                    "distance NUMERIC," +
                    "heuristic TEXT," +
                    "tick NUMERIC" +
                    ")";
            stmnt.execute(sqlMakeCarTable);
            String sqlMakeParkingTable = "CREATE TABLE IF NOT EXISTS parking_spots (" +
                    "id NUMERIC," +
                    "distance NUMERIC," +
                    "occupied NUMERIC," +
                    "tick NUMERIC" +
                    ")";
            stmnt.execute(sqlMakeParkingTable);
            stmnt.close();
            LOG.info("Initialization done");
        } catch (ClassNotFoundException e) {
            LOG.error("Could not load sqlite driver!\n Fallback to Logger");
        } catch (SQLException e) {
            LOG.error("Encountered a SQLException:{}", e.getMessage());
        }

    }

    @Override
    public void putParkingData(int id, int distance, boolean occupied, long tick) {
        String sqlInsertParkingData = "INSERT INTO parking_spots (id,distance,occupied,tick) VALUES (" + id + "," + distance + "," + (occupied ? 1 : 0) + "," + tick + ")";
        putData(sqlInsertParkingData);
        LOG.debug("{}::{}|{}|{}", "ParkingSpot", id, (occupied ? 1 : 0), tick);
    }

    @Override
    public void putCarData(int id, int parkingSlotId, int distance, String heuristic, long tick) {
        String sqlInsertCarData = "INSERT INTO cars (id,parkingSlotId, distance, heuristic, tick) VALUES(" + id + "," + parkingSlotId + "," + distance + ",'" + heuristic + "'," + tick + ")";
        putData(sqlInsertCarData);
        LOG.debug("{}::{}|{}|{}|{}|[}", "Car", id, parkingSlotId, distance, heuristic, tick);
    }

    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            LOG.warn(e.getMessage());
        }
    }

    private void putData(String sql) {
        if (connection != null) {
            try {
                if (batchStatement == null || batchStatement.isClosed()) {
                    batchStatement = connection.createStatement();
                }
                batchStatement.addBatch(sql);
                batchSize++;
                if(batchSize == batchThreshold){
                    LOG.info("Executed SQL Batch");
                    batchSize = 0;
                    batchStatement.executeLargeBatch();
                    batchStatement.close();
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
    }
}
