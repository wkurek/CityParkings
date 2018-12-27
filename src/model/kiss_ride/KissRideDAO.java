package model.kiss_ride;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.location.LocationDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class KissRideDAO {
    private static class KissRideContract {
        static final String TABLE_NAME = "[dbo].[kiss_rides]";
        static final String COLUMN_NAME_ID = "parking_id";
        static final String COLUMN_NAME_MAX_STOP = "max_stop";
        static final String COLUMN_NAME_ARE_GATES = "are_gates";
    }

    private static String generateSelectQuery() {
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, " +
                        "%s.%s, %s.%s FROM %s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s",
                KissRideContract.TABLE_NAME, KissRideContract.COLUMN_NAME_MAX_STOP,
                KissRideContract.TABLE_NAME, KissRideContract.COLUMN_NAME_ARE_GATES,

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

                KissRideContract.TABLE_NAME, ParkingDAO.ParkingContract.TABLE_NAME,
                KissRideContract.TABLE_NAME, KissRideContract.COLUMN_NAME_ID,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_ID,

                LocationDAO.LocationContract.TABLE_NAME,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_LOCATION_ID,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID
        );
    }

    public static KissRide generateKissRide(CachedRowSet resultSet) throws SQLException {
        KissRide kissRide = new KissRide();

        Parking parking = ParkingDAO.generateParking(resultSet);
        kissRide.setParking(parking);

        kissRide.setAreGates(resultSet.getBoolean(KissRideContract.COLUMN_NAME_ARE_GATES));
        kissRide.setMaxStop(resultSet.getDate(KissRideContract.COLUMN_NAME_MAX_STOP));

        return kissRide;
    }

    private static ObservableList<KissRide> generateKissRideList(CachedRowSet resultSet) throws SQLException {
        ObservableList<KissRide> kissRideList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            KissRide kissRide = generateKissRide(resultSet);
            kissRideList.add(kissRide);
        }

        return kissRideList;
    }

    public static ObservableList<KissRide> getKissRides() {
        String sql = generateSelectQuery();
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<KissRide> kissRideList = null;

        try {
            kissRideList = generateKissRideList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return kissRideList;
    }

    public static KissRide getKissRide(int id) {
        String sql = String.format("%s WHERE %s.%s=%d", generateSelectQuery(), KissRideContract.TABLE_NAME,
                KissRideContract.COLUMN_NAME_ID, id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        KissRide kissRide = null;

        try {
            kissRide = generateKissRide(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return kissRide;
    }

    public static void saveKissRide(KissRide kissRide) {
        if(kissRide == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (0, '%s', %b)", KissRideContract.TABLE_NAME,
                kissRide.getMaxStop(), kissRide.isAreGates());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateKissRide(int id, KissRide updatedKissRide) {
        if(updatedKissRide == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s', %s = %b WHERE %s = %d", KissRideContract.TABLE_NAME,
                KissRideContract.COLUMN_NAME_MAX_STOP, updatedKissRide.getMaxStop(),
                KissRideContract.COLUMN_NAME_ARE_GATES, updatedKissRide.isAreGates(),
                KissRideContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteKissRide(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d", KissRideContract.TABLE_NAME,
                KissRideContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
