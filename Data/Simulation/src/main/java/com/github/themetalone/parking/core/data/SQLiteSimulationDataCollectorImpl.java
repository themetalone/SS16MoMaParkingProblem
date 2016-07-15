package com.github.themetalone.parking.core.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.*;

/**
 * Writes data to a H2 sql database. The data is first put into batches of 1000 entries each. If a batch is full the data is put to the database and the batch is cleared.
 * Created by steff on 12.07.2016.
 */
public class SQLiteSimulationDataCollectorImpl implements SimulationDataCollector {

    private Logger LOG = LoggerFactory.getLogger(SQLiteSimulationDataCollectorImpl.class);
    private Connection connection = null;
    private PreparedStatement parkingDataBatchStatement = null;
    private PreparedStatement carDataBatchStatement = null;
    private PreparedStatement carBatchStatement = null;
    private PreparedStatement parkingBatchStatement = null;
    private long batchThreshold = 10000;
    private long parkingDataBatchSize = 0;
    private long carDataBatchSize = 0;
    private long carBatchSize = 0;
    private long parkingBatchSize = 0;
    private boolean[] parkingState = null;
    private int numberOfParkingSlots = 0;
    private long currentTick = 0;
    private String lastParkingState = "";

    /**
     * Writes unprocessed statements in the batch to the database and closes the connection
     */
    @Override
    public void close() {
        LOG.info("Writing batches to db");
        finalBatchExecution(parkingBatchStatement);
        finalBatchExecution(carBatchStatement);
        finalBatchExecution(parkingDataBatchStatement);
        finalBatchExecution(carDataBatchStatement);
        try {
            LOG.info("Closing Database");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOG.info("Done!");

    }

    private void finalBatchExecution(Statement stmnt) {
        try {
            if (stmnt != null) {
                stmnt.executeBatch();
                stmnt.close();
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes a database at the location. The database is initialized with
     * <ul>
     * <li>usr:simulation, pw:simulation</li>
     * <li>schema PARKINGSIMULATION (access by usr:simulation)</li>
     * <li>table PARKINGSIMULATION.CARS</li>
     * <li>table PARKINGSIMULATION.PARKING_SPOTS</li>
     * <li>table PARKINGSIMULATION.CARDATA</li>
     * <li>table PARKINGSIMULATION.PARKINGDATA</li>
     * </ul>
     *
     * @param location of the h2 database
     */
    public SQLiteSimulationDataCollectorImpl(String location) {
        String jdbcPrefix = "jdbc:h2";
        String jdbcUrl = jdbcPrefix + ":" + location + ";MV_STORE=FALSE;MVCC=FALSE";
        try {
            String jdbcDriver = "org.h2.Driver";
            Class.forName(jdbcDriver);
            Connection initConnection = DriverManager.getConnection(jdbcUrl, "", "");
            Statement stmnt = initConnection.createStatement();
            stmnt.addBatch("CREATE USER IF NOT EXISTS simulation PASSWORD 'simulation' ADMIN");
            stmnt.addBatch("CREATE SCHEMA IF NOT EXISTS PARKINGSIMULATION AUTHORIZATION simulation");
            stmnt.addBatch("CREATE TABLE IF NOT EXISTS PARKINGSIMULATION.CARS (ID INTEGER PRIMARY KEY ,HEURISTIC VARCHAR(255));");
            stmnt.addBatch("CREATE TABLE IF NOT EXISTS PARKINGSIMULATION.PARKING_SPOTS (ID INTEGER PRIMARY KEY ,DISTANCE INTEGER);");
            stmnt.addBatch("CREATE TABLE IF NOT EXISTS PARKINGSIMULATION.CARDATA (ID INTEGER ,DISTANCE INTEGER,TICK BIGINT, PRIMARY KEY (ID,TICK));");
            stmnt.addBatch("CREATE TABLE IF NOT EXISTS PARKINGSIMULATION.PARKINGDATA (STATE VARCHAR(255),TICK BIGINT PRIMARY KEY);");
            stmnt.executeBatch();
            stmnt.close();
            connection = DriverManager.getConnection(jdbcUrl, "simulation", "simulation");
            LOG.info("Storage Initialization done");
        } catch (ClassNotFoundException e) {
            LOG.error("Could not load sqlite driver!\n Fallback to Logger");
        } catch (SQLException e) {
            LOG.error("Encountered a SQLException:{}", e.getMessage());
        }

    }

    @Override
    public void putParkingSlot(int id, int distance) {
        numberOfParkingSlots = ((numberOfParkingSlots < id + 1) ? id + 1 : numberOfParkingSlots);
        try {
            if (parkingBatchStatement == null || parkingBatchStatement.isClosed()) {
                parkingBatchStatement = connection.prepareStatement("INSERT INTO PARKINGSIMULATION.PARKING_SPOTS (ID,DISTANCE) VALUES (?,?);");
            }
            parkingBatchStatement.setInt(1, id);
            parkingBatchStatement.setInt(2, distance);
            parkingBatchStatement.addBatch();
            parkingBatchSize++;
            if (parkingBatchSize == batchThreshold) {
                parkingBatchSize = 0;
                executeBatch(parkingBatchStatement);
            }
        } catch (SQLException e) {
            LOG.warn("Encountered a SQLException:{}", e.getMessage());
        }
    }

    @Override
    public void putCar(int id, String heuristic) {
        try {
            if (carBatchStatement == null || carBatchStatement.isClosed()) {
                carBatchStatement = connection.prepareStatement("INSERT INTO PARKINGSIMULATION.CARS (ID, HEURISTIC) VALUES (?,?);");
            }
            carBatchStatement.setInt(1, id);
            carBatchStatement.setString(2, heuristic);
            carBatchStatement.addBatch();
            carBatchSize++;
            if (carBatchSize == batchThreshold) {
                carBatchSize = 0;
                executeBatch(carBatchStatement);
            }
        } catch (SQLException e) {
            LOG.warn("Encountered a SQLException:{}", e.getMessage());
        }
    }

    @Override
    public void putParkingData(int id, boolean occupied, long tick) {
        if (parkingState == null) {
            LOG.debug("Coding parking spot states in {}bit Integer", numberOfParkingSlots);
            parkingState = new boolean[numberOfParkingSlots];
            currentTick = tick;
        }
        if (currentTick != tick) {
            LOG.debug(DataUtil.booleanArrayToString(parkingState));
            currentTick = tick;
            String parkingStateString = new BigInteger(DataUtil.toBytes(parkingState)).toString();
            LOG.debug("Putting parking data at tick {}:LastState:{} , NewState:{}", tick, lastParkingState, parkingStateString);
            if (!parkingStateString.equals(lastParkingState)) {
                lastParkingState = parkingStateString;
                try {
                    if (parkingDataBatchStatement == null || parkingDataBatchStatement.isClosed()) {
                        parkingDataBatchStatement = connection.prepareStatement("INSERT INTO PARKINGSIMULATION.PARKINGDATA (STATE,TICK) VALUES (?,?);");
                    }
                    parkingDataBatchStatement.setString(1, parkingStateString);
                    parkingDataBatchStatement.setLong(2, tick);
                    parkingDataBatchStatement.addBatch();
                    parkingDataBatchSize++;
                    if (parkingDataBatchSize == batchThreshold) {
                        parkingDataBatchSize = 0;
                        executeBatch(parkingDataBatchStatement);
                    }
                } catch (SQLException e) {
                    LOG.warn("Encountered a SQLException:{}", e.getMessage());
                }
            }
            parkingState = new boolean[numberOfParkingSlots];
        }
        parkingState[id] = occupied;
    }

    @Override
    public void putCarData(int id, int distance, long tick) {
        String sqlInsertCarData = "INSERT INTO PARKINGSIMULATION.CARDATA (ID,DISTANCE,TICK) VALUES(?,?,?);";
        try {
            if (carDataBatchStatement == null || carDataBatchStatement.isClosed()) {
                carDataBatchStatement = connection.prepareStatement(sqlInsertCarData);
            }
            carDataBatchStatement.setInt(1, id);
            carDataBatchStatement.setInt(2, distance);
            carDataBatchStatement.setLong(3, tick);
            carDataBatchStatement.addBatch();
            carDataBatchSize++;
            if (carDataBatchSize == batchThreshold) {
                carDataBatchSize = 0;
                executeBatch(carDataBatchStatement);
            }
        } catch (SQLException e) {
            LOG.warn("Encountered a SQLException:{}", e.getMessage());
        }

    }

    private void executeBatch(Statement stmt) throws SQLException {
        stmt.executeBatch();
        connection.commit();
        stmt.clearBatch();
    }

}
