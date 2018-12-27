package model.location;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class LocationDAO {
    private static class LocationContract {
        static final String TABLE_NAME = "[dbo].[locations]";
        static final String COLUMN_NAME_LATITUDE = "latitude";
        static final String COLUMN_NAME_LONGITUDE = "longitude";
    }

    public static Location generateLocation(CachedRowSet resultSet) throws SQLException {
        Location location = new Location();
        location.setLatitude(resultSet.getFloat(LocationDAO.LocationContract.COLUMN_NAME_LATITUDE));
        location.setLongitude(resultSet.getFloat(LocationDAO.LocationContract.COLUMN_NAME_LONGITUDE));

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

    public static Location getLocation(float locationLatitude, float locationLongitude) {
        String sql = String.format("SELECT * FROM %s WHERE %s=%.2f AND %s=%.2f", LocationContract.TABLE_NAME,
                LocationContract.COLUMN_NAME_LATITUDE, locationLatitude, LocationContract.COLUMN_NAME_LONGITUDE, locationLongitude);

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

        String sql = String.format("INSERT INTO %s VALUES ('%.2f, %.2f')", LocationContract.TABLE_NAME, location.getLatitude(),
                location.getLongitude());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteLocation(float locationLatitude, float locationLongitude) {
        String sql = String.format("DELETE FROM %s WHERE %s = %.2f AND %s = %.2f", LocationContract.TABLE_NAME,
                LocationContract.COLUMN_NAME_LATITUDE, locationLatitude, LocationContract.COLUMN_NAME_LONGITUDE, locationLongitude);

        DbHelper.executeUpdateQuery(sql);
    }
}
