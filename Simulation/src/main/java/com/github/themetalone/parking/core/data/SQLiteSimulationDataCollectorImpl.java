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

    private final String jdbcPrefix = "jdbc:sqlite";
    private final String jdbcDriver = "org.sqlite.DBC";
    private final String jdbcUrl;
    private final String sqlMakeParkingTable = "CREATE TABLE IF NOT EXISTS parking_spots (" +
            "id Numeric," +
            "distance Numeric" +
            "occupied Numeric," +
            "tick Numeric" +
            ")";
    private final String sqlMakeCarTable = "CREATE TABLE IF NOT EXISTS cars (" +
            "id Numeric," +
            "parkingSlot Numeric," +
            "distance Numeric," +
            "heuristic TEXT," +
            "tick Numeric" +
            ")";
    private Logger LOG = LoggerFactory.getLogger(SQLiteSimulationDataCollectorImpl.class);
    private Connection connection = null;

    public SQLiteSimulationDataCollectorImpl(String location) {
        jdbcUrl = jdbcPrefix + ":" + location;
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl);
            Statement stmnt = connection.createStatement();
            stmnt.execute(sqlMakeCarTable);
            stmnt.execute(sqlMakeParkingTable);
            stmnt.close();
        } catch (ClassNotFoundException e) {
            LOG.error("Could not load sqlite driver!\n Fallback to Logger");
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }

    }

    @Override
    public void putParkingData(int id, int distance, boolean occupied, long tick) {
        String sqlInsertParkingData = "INSERT INTO parking_spots (id,distance,occupied,tick) VALUES (" + id + "," + distance + "," + (occupied ? 1 : 0) + "," + tick + ")";
        putData(sqlInsertParkingData);
        LOG.info("{}::{}|{}|{}", "ParkingSpot", id, (occupied ? 1 : 0), tick);
    }

    @Override
    public void putCarData(int id, int parkingSlotId, int distance, String heuristic, long tick) {
        String sqlInsertCarData = "INSERT INTO cars (id,parkingSlotId, distance, heuristic, tick) VALUES(" + id + "," + parkingSlotId + "," + distance + ",'" + heuristic + "'," + tick + ")";
        putData(sqlInsertCarData);
        LOG.info("{}::{}|{}|{}|{}|[}", "Car", id, parkingSlotId, distance, heuristic, tick);
    }

    private void putData(String sql) {
        if (connection != null) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.execute(sql);
                statement.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
    }
}
