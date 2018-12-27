package model.park_ride;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class ParkRideDAO {
    public static class parkRideContract {
        public static final String TABLE_NAME = "[dbo].[park_rides]";
        public static final String COLUMN_NAME_ID = "parking_id";
        public static final String COLUMN_NAME_IS_AUTOMATIC = "is_automatic";
        public static final String COLUMN_NAME_COMMUNICATION_NODE = "communication_node";
    }

    public static ParkRide generateParkRide(CachedRowSet resultSet) throws SQLException {
        ParkRide parkRide = new ParkRide();

        Parking parking = ParkingDAO.generateParking(resultSet);
        parkRide.setParking(parking);

        parkRide.setIsAutomatic(resultSet.getBoolean(parkRideContract.COLUMN_NAME_IS_AUTOMATIC));
        parkRide.setCommunicationNode(resultSet.getString(parkRideContract.COLUMN_NAME_COMMUNICATION_NODE));

        return parkRide;
    }

    private static ObservableList<ParkRide> generateParkRideList(CachedRowSet resultSet) throws SQLException {
        ObservableList<ParkRide> parkRideList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            ParkRide parkRide = generateParkRide(resultSet);
            parkRideList.add(parkRide);
        }

        return parkRideList;
    }

    public static ObservableList<ParkRide> getParkRides() {
        String sql = String.format("SELECT * FROM %s", parkRideContract.TABLE_NAME);
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<ParkRide> parkRideList = null;

        try {
            parkRideList = generateParkRideList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return parkRideList;
    }

    public static ParkRide getParkRide(int id) {
        String sql = String.format("SELECT * FROM %s WHERE %s=%d", parkRideContract.TABLE_NAME,
                parkRideContract.COLUMN_NAME_ID, id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        ParkRide parkRide = null;

        try {
            parkRide = generateParkRide(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return parkRide;
    }

    public static void saveParkRide(ParkRide parkRide) {
        if(parkRide == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (0, %b, '%s')", parkRideContract.TABLE_NAME, parkRide.isIsAutomatic(),
                parkRide.getCommunicationNode());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateParkRide(int id, ParkRide updatedParkRide) {
        if(updatedParkRide == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = %b, %s = '%s' WHERE %s = %d", parkRideContract.TABLE_NAME,
                parkRideContract.COLUMN_NAME_IS_AUTOMATIC, updatedParkRide.isIsAutomatic(),
                parkRideContract.COLUMN_NAME_COMMUNICATION_NODE, updatedParkRide.getCommunicationNode(),
                parkRideContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteParkRide(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d", parkRideContract.TABLE_NAME,
                parkRideContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
