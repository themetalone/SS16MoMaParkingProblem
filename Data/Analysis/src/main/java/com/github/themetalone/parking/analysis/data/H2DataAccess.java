package com.github.themetalone.parking.analysis.data;

import com.github.themetalone.parking.analysis.exceptions.NoFurtherCar;
import com.github.themetalone.parking.analysis.exceptions.NoFurtherObject;
import com.github.themetalone.parking.analysis.model.CarEntry;
import com.github.themetalone.parking.analysis.model.ParkingStateEntry;
import com.github.themetalone.parking.core.car.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by steff on 17.07.2016.
 */
public class H2DataAccess implements DataAccess {

    private Logger LOG = LoggerFactory.getLogger(DataAccess.class);
    private Connection connection;
    private ResultSet cars;
    private ResultSet pslots;

    private boolean[][] parking_slots;


    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public H2DataAccess(String location) {
        String jdbcPrefix = "jdbc:h2";
        String jdbcUrl = jdbcPrefix + ":" + location + ";MV_STORE=FALSE;MVCC=FALSE";
        String usr = "simulation";
        String pw = "simulation";
        String jdbcDriver = "org.h2.Driver";
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl, usr, pw);
            Statement statement = connection.createStatement();
            cars = statement.executeQuery("SELECT PARKINGSIMULATION.CARS.ID, PARKINGSIMULATION.CARDATA.DISTANCE, PARKINGSIMULATION.CARDATA.TICK, PARKINGSIMULATION.CARS.HEURISTIC FROM PARKINGSIMULATION.CARDATA INNER JOIN PARKINGSIMULATION.CARS ON PARKINGSIMULATION.CARS.ID = PARKINGSIMULATION.CARDATA.ID ");
            pslots = statement.executeQuery("SELECT PARKINGSIMULATION.PARKINGDATA.STATE, PARKINGSIMULATION.PARKINGDATA.TICK FROM PAKRINGSIMULATION.PARKINGDATA");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public CarEntry nextCar() throws NoFurtherCar {
        try {
            if (cars.next()) {
                int id = cars.getInt(1);
                int dist = cars.getInt(2);
                long tick = cars.getLong(3);
                String heuristic = cars.getString(4);
                return new CarEntry(id, dist, tick, heuristic);
            }else{
                throw new NoFurtherCar();
            }
        } catch (SQLException e) {
            throw new NoFurtherCar();
        }
    }

    private void makeParkingStates(){

    }

    public static boolean[] booleanArrayFromByteArray(byte[] x) {
        boolean[] y = new boolean[x.length * 8];
        int position = 0;
        for(byte z : x) {
            boolean[] temp = booleanArrayFromByte(z);
            System.arraycopy(temp, 0, y, position, 8);
            position += 8;
        }
        return y;
    }

    public static boolean[] booleanArrayFromByte(byte x) {
        boolean bs[] = new boolean[4];
        bs[0] = ((x & 0x01) != 0);
        bs[1] = ((x & 0x02) != 0);
        bs[2] = ((x & 0x04) != 0);
        bs[3] = ((x & 0x08) != 0);
        return bs;
    }

    @Override
    public ParkingStateEntry nextParkingState() throws NoFurtherObject {
        return null;
    }

}
