package model.kiss_ride;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class KissRideDAO {
    public static class kissRideContract {
        public static final String TABLE_NAME = "[dbo].[kiss_rides]";
        public static final String COLUMN_NAME_ID = "parking_id";
        public static final String COLUMN_NAME_MAX_STOP = "max_stop";
        public static final String COLUMN_NAME_ARE_GATES = "are_gates";
    }

    public static KissRide generateKissRide(CachedRowSet resultSet) throws SQLException {
        KissRide kissRide = new KissRide();

        Parking parking = ParkingDAO.generateParking(resultSet);
        kissRide.setParking(parking);

        kissRide.setAreGates(resultSet.getBoolean(kissRideContract.COLUMN_NAME_ARE_GATES));
        kissRide.setMaxStop(resultSet.getDate(kissRideContract.COLUMN_NAME_MAX_STOP));

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
        String sql = String.format("SELECT * FROM %s", kissRideContract.TABLE_NAME);
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
        String sql = String.format("SELECT * FROM %s WHERE %s=%d", kissRideContract.TABLE_NAME,
                kissRideContract.COLUMN_NAME_ID, id);

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

        String sql = String.format("INSERT INTO %s VALUES (0, '%s', %b)", kissRideContract.TABLE_NAME, kissRide.getMaxStop(),
                kissRide.isAreGates());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateKissRide(int id, KissRide updatedKissRide) {
        if(updatedKissRide == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s', %s = %b WHERE %s = %d", kissRideContract.TABLE_NAME,
                kissRideContract.COLUMN_NAME_MAX_STOP, updatedKissRide.getMaxStop(),
                kissRideContract.COLUMN_NAME_ARE_GATES, updatedKissRide.isAreGates(),
                kissRideContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteKissRide(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d", kissRideContract.TABLE_NAME,
                kissRideContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
