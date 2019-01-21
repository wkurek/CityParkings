package model.location;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.Locale;

public class LocationDAO {
    public static class LocationContract {
        public static final String TABLE_NAME = "[dbo].[locations]";
        public static final String COLUMN_NAME_ID = "location_id";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }

    public static Location generateLocation(CachedRowSet resultSet) throws SQLException {
        Location location = new Location();
        location.setLocationId(resultSet.getInt(LocationContract.COLUMN_NAME_ID));
        location.setLatitude(resultSet.getFloat(LocationContract.COLUMN_NAME_LATITUDE));
        location.setLongitude(resultSet.getFloat(LocationContract.COLUMN_NAME_LONGITUDE));

        return location;
    }

    private static ObservableList<Location> generateLocationList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Location> locationList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Location location = generateLocation(resultSet);
            locationList.add(location);
        }

        return locationList;
    }

    public static ObservableList<Location> getLocation() {
        String sql = String.format("SELECT * FROM %s", LocationContract.TABLE_NAME);
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Location> locationList = null;

        try {
            locationList = generateLocationList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return locationList;
    }
    public static Integer getSavedLocationId()
    {
        String sql = String.format("SELECT IDENT_CURRENT ('%s') AS %s", LocationContract.TABLE_NAME,
                LocationContract.COLUMN_NAME_ID);
        CachedRowSet result = DbHelper.executeQuery(sql);

        Integer index = null;

        try {
            if(result.next()) {
                index = result.getInt(LocationContract.COLUMN_NAME_ID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    public static Location getLocation(int locationId) {
        String sql = String.format("SELECT * FROM %s WHERE %s=%d", LocationContract.TABLE_NAME,
                LocationContract.COLUMN_NAME_ID, locationId);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Location location = null;

        try {
            location = generateLocation(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return location;
    }

    public static void saveLocation(Location location) {
        if(location == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format(Locale.US,"INSERT INTO %s VALUES (%.2f, %.2f)", LocationContract.TABLE_NAME,
                location.getLatitude(), location.getLongitude());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateLocation(int locationId, Location updatedLocation) {
        if(updatedLocation == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = %.2f, %s = %.2f WHERE %s = %d", LocationContract.TABLE_NAME,
                LocationContract.COLUMN_NAME_LATITUDE, updatedLocation.getLatitude(),
                LocationContract.COLUMN_NAME_LONGITUDE, updatedLocation.getLongitude(),
                LocationContract.COLUMN_NAME_ID, updatedLocation.getLocationId());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteLocation(int locationId) {
        String sql = String.format("DELETE FROM %s WHERE %s=%d", LocationContract.TABLE_NAME,
                LocationContract.COLUMN_NAME_ID, locationId);

        DbHelper.executeUpdateQuery(sql);
    }
}
