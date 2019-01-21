package model.park_ride;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.location.LocationDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class ParkRideDAO {
    private static class ParkRideContract {
        static final String TABLE_NAME = "[dbo].[park_rides]";
        static final String COLUMN_NAME_ID = "parking_id";
        static final String COLUMN_NAME_IS_AUTOMATIC = "is_automatic";
        static final String COLUMN_NAME_COMMUNICATION_NODE = "communication_node";
    }

    private static String generateSelectQuery() {
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, " +
                        "%s.%s, %s.%s FROM %s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s",
                ParkRideContract.TABLE_NAME, ParkRideContract.COLUMN_NAME_IS_AUTOMATIC,
                ParkRideContract.TABLE_NAME, ParkRideContract.COLUMN_NAME_COMMUNICATION_NODE,

                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_ID,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_STANDARD_LOTS,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_DISABLED_LOTS,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_IS_ROOF,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_IS_GUARDED,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_LAST_CONTROL,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_WEIGHT,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_HEIGHT,

                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_LATITUDE,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_LONGITUDE,

                ParkRideContract.TABLE_NAME, ParkingDAO.ParkingContract.TABLE_NAME,
                ParkRideContract.TABLE_NAME, ParkRideContract.COLUMN_NAME_ID,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_ID,

                LocationDAO.LocationContract.TABLE_NAME,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_LOCATION_ID,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID
        );
    }

    public static ParkRide generateParkRide(CachedRowSet resultSet) throws SQLException {
        ParkRide parkRide = new ParkRide();

        Parking parking = ParkingDAO.generateParking(resultSet);
        parkRide.setParking(parking);

        parkRide.setAutomatic(resultSet.getBoolean(ParkRideContract.COLUMN_NAME_IS_AUTOMATIC));
        parkRide.setCommunicationNode(resultSet.getString(ParkRideContract.COLUMN_NAME_COMMUNICATION_NODE));

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
        String sql = generateSelectQuery();
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
        String sql = String.format("%s WHERE %s.%s=%d", generateSelectQuery(), ParkRideContract.TABLE_NAME,
                ParkRideContract.COLUMN_NAME_ID, id);

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

        String sql = String.format("INSERT INTO %s VALUES (%d, %d, '%s')", ParkRideContract.TABLE_NAME, parkRide.getParking().getParkingId(),
                btoi(parkRide.isAutomatic()), parkRide.getCommunicationNode());

        DbHelper.executeUpdateQuery(sql);
    }

    private static Integer btoi(boolean bool) {
        return bool ? 1 : 0;
    }

    public static void updateParkRide(int id, ParkRide updatedParkRide) {
        if(updatedParkRide == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = %b, %s = '%s' WHERE %s = %d", ParkRideContract.TABLE_NAME,
                ParkRideContract.COLUMN_NAME_IS_AUTOMATIC, updatedParkRide.isAutomatic(),
                ParkRideContract.COLUMN_NAME_COMMUNICATION_NODE, updatedParkRide.getCommunicationNode(),
                ParkRideContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteParkRide(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d", ParkRideContract.TABLE_NAME,
                ParkRideContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
