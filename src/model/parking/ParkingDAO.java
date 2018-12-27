package model.parking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.location.Location;
import model.location.LocationDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class ParkingDAO {
    private static class ParkingContract {
        static final String TABLE_NAME = "[dbo].[parkings]";
        static final String COLUMN_NAME_ID = "parking_id";
        static final String COLUMN_NAME_STANDARD_LOTS = "standard_lots";
        static final String COLUMN_NAME_DISABLED_LOTS = "disabled_lots";
        static final String COLUMN_NAME_IS_ROOF = "is_roof";
        static final String COLUMN_NAME_IS_GUARDED = "is_guarded";
        static final String COLUMN_NAME_LAST_CONTROL = "last_control";
        static final String COLUMN_NAME_WEIGHT = "max_weight";
        static final String COLUMN_NAME_HEIGHT = "max_height";
        static final String COLUMN_NAME_LATITUDE = "latitude";
        static final String COLUMN_NAME_LONGITUDE = "longitude";
    }

    public static Parking generateParking(CachedRowSet resultSet) throws SQLException {
        Parking parking = new Parking();

        Location location = LocationDAO.generateLocation(resultSet);
        parking.setLocation(location);

        parking.setParkingId(resultSet.getInt(ParkingContract.COLUMN_NAME_ID));
        parking.setStandardLots(resultSet.getInt(ParkingContract.COLUMN_NAME_STANDARD_LOTS));
        parking.setDisabledLots(resultSet.getInt(ParkingContract.COLUMN_NAME_DISABLED_LOTS));
        parking.setIsRoof(resultSet.getBoolean(ParkingContract.COLUMN_NAME_IS_ROOF));
        parking.setIsGuarded(resultSet.getBoolean(ParkingContract.COLUMN_NAME_IS_GUARDED));
        parking.setLastControl(resultSet.getDate(ParkingContract.COLUMN_NAME_LAST_CONTROL));
        parking.setMaxWeight(resultSet.getFloat(ParkingContract.COLUMN_NAME_WEIGHT));
        parking.setMaxHeight(resultSet.getFloat(ParkingContract.COLUMN_NAME_HEIGHT));

        return parking;
    }

    private static ObservableList<Parking> generateParkingsList(CachedRowSet resultSet) throws SQLException{
        ObservableList<Parking> parkingList = FXCollections.observableArrayList();

        while(resultSet.next()){
            Parking parking = generateParking(resultSet);
            parkingList.add(parking);
        }
        return parkingList;
    }


    private static String generateSelectQuery() {
        //@TODO
    }



    private static String generateSelectWhereQuery(int id) {
        String sql = generateSelectQuery();
        return String.format("%s WHERE %s.%s=%d", sql, ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_ID, id);
    }

    public static ObservableList<Parking> getParkings() {
        String sql = generateSelectQuery();

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Parking> parkingList = null;

        try {
            parkingList = generateParkingsList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return parkingList;
    }

    public static Parking getParking(int id) {
        String sql = generateSelectWhereQuery(id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Parking parking = null;

        try {
            parking = generateParking(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return parking;
    }

    public static void saveParking(Parking parking) {
        if(parking == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (0, %d, %d, %b, %b, '%s', " +
                        "%.2f, %.2f, %.2f, %.2f)",
                ParkingContract.TABLE_NAME, parking.getStandardLots(), parking.getDisabledLots(),
                parking.isIsRoof(), parking.isIsGuarded(), parking.getLastControl(),
                parking.getMaxWeight(), parking.getMaxHeight(), parking.getLocation().getLatitude(),
                parking.getLocation().getLongitude());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateParking(int id, Parking updatedParking) {
        if (updatedParking == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = %d, %s = %d, %s = %b, %s = %b, %s = '%s', " +
                        "%s = %.2f, %s = %.2f, %s = %.2f, %s = %.2f WHERE %s = %d",
                ParkingContract.TABLE_NAME,
                ParkingContract.COLUMN_NAME_STANDARD_LOTS, updatedParking.getStandardLots(),
                ParkingContract.COLUMN_NAME_DISABLED_LOTS, updatedParking.getDisabledLots(),
                ParkingContract.COLUMN_NAME_IS_ROOF, updatedParking.isIsRoof(),
                ParkingContract.COLUMN_NAME_IS_GUARDED, updatedParking.isIsGuarded(),
                ParkingContract.COLUMN_NAME_LAST_CONTROL, updatedParking.getLastControl(),
                ParkingContract.COLUMN_NAME_WEIGHT, updatedParking.getMaxWeight(),
                ParkingContract.COLUMN_NAME_HEIGHT, updatedParking.getMaxHeight(),
                ParkingContract.COLUMN_NAME_LATITUDE, updatedParking.getLocation().getLatitude(),
                ParkingContract.COLUMN_NAME_LONGITUDE, updatedParking.getLocation().getLongitude(),
                ParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteParking(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d",ParkingContract.TABLE_NAME,
                ParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

}
