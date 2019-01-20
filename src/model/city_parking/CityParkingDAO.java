package model.city_parking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.location.LocationDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class CityParkingDAO {
    private static class CityParkingContract {
        static final String TABLE_NAME = "[dbo].[city_parkings]";
        static final String COLUMN_NAME_ID = "parking_id";
        static final String COLUMN_NAME_PARK_TYPE = "park_type";
        static final String COLUMN_NAME_IS_ZONE_PAID = "is_zone_paid";
    }

    private static String generateSelectQuery() {
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, " +
                        "%s.%s, %s.%s FROM %s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s",
                CityParkingContract.TABLE_NAME, CityParkingContract.COLUMN_NAME_PARK_TYPE,
                CityParkingContract.TABLE_NAME, CityParkingContract.COLUMN_NAME_IS_ZONE_PAID,

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

                CityParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.TABLE_NAME,
                CityParkingContract.TABLE_NAME, CityParkingContract.COLUMN_NAME_ID,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_ID,

                LocationDAO.LocationContract.TABLE_NAME,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_LOCATION_ID,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID
        );
    }

    public static CityParking generateCityParking(CachedRowSet resultSet) throws SQLException {
        CityParking cityParking = new CityParking();

        Parking parking = ParkingDAO.generateParking(resultSet);
        cityParking.setParking(parking);

        cityParking.setZonePaid(resultSet.getBoolean(CityParkingContract.COLUMN_NAME_IS_ZONE_PAID));
        cityParking.setParkType(resultSet.getString(CityParkingContract.COLUMN_NAME_PARK_TYPE));

        return cityParking;
    }

    private static ObservableList<CityParking> generateCityParkingList(CachedRowSet resultSet) throws SQLException {
        ObservableList<CityParking> cityParkingList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            CityParking cityParking = generateCityParking(resultSet);
            cityParkingList.add(cityParking);
        }

        return cityParkingList;
    }

    public static ObservableList<CityParking> getCityParkings() {
        String sql = generateSelectQuery();
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<CityParking> cityParkingList = null;

        try {
            cityParkingList = generateCityParkingList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return cityParkingList;
    }

    public static CityParking getCityParking(int id) {
        String sql = String.format("%s WHERE %s.%s=%d", generateSelectQuery(), CityParkingContract.TABLE_NAME,
                CityParkingContract.COLUMN_NAME_ID, id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        CityParking cityParking = null;

        try {
            cityParking = generateCityParking(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return cityParking;
    }

    public static void saveCityParking(CityParking cityParking) {
        if(cityParking == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES ('%d', '%s', %b)", CityParkingContract.TABLE_NAME, cityParking.getParking().getParkingId(),
                cityParking.getParkType(), cityParking.isZonePaid());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateCityParking(int id, CityParking updatedCityParking) {
        if(updatedCityParking == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s', %s = %b WHERE %s = %d", CityParkingContract.TABLE_NAME,
                CityParkingContract.COLUMN_NAME_PARK_TYPE, updatedCityParking.getParkType(),
                CityParkingContract.COLUMN_NAME_IS_ZONE_PAID, updatedCityParking.isZonePaid(),
                CityParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteCityParking(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d", CityParkingContract.TABLE_NAME,
                CityParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
