package model.city_parking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class CityParkingDAO {
    public static class cityParkingContract {
        public static final String TABLE_NAME = "[dbo].[city_parkings]";
        public static final String COLUMN_NAME_ID = "parking_id";
        public static final String COLUMN_NAME_PARK_TYPE = "park_type";
        public static final String COLUMN_NAME_IS_ZONE_PAID = "is_zone_paid";
    }

    public static CityParking generateCityParking(CachedRowSet resultSet) throws SQLException {
        CityParking cityParking = new CityParking();

        Parking parking = ParkingDAO.generateParking(resultSet);
        cityParking.setParking(parking);

        cityParking.setIsZonePaid(resultSet.getBoolean(cityParkingContract.COLUMN_NAME_IS_ZONE_PAID));
        cityParking.setParkType(resultSet.getString(cityParkingContract.COLUMN_NAME_PARK_TYPE));

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
        String sql = String.format("SELECT * FROM %s", cityParkingContract.TABLE_NAME);
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
        String sql = String.format("SELECT * FROM %s WHERE %s=%d", cityParkingContract.TABLE_NAME,
                cityParkingContract.COLUMN_NAME_ID, id);

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

        String sql = String.format("INSERT INTO %s VALUES (0, '%s', %b)", cityParkingContract.TABLE_NAME, cityParking.getParkType(),
                cityParking.isIsZonePaid());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateCityParking(int id, CityParking updatedCityParking) {
        if(updatedCityParking == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s', %s = %b WHERE %s = %d", cityParkingContract.TABLE_NAME,
                cityParkingContract.COLUMN_NAME_PARK_TYPE, updatedCityParking.getParkType(),
                cityParkingContract.COLUMN_NAME_IS_ZONE_PAID, updatedCityParking.isIsZonePaid(),
                cityParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteCityParking(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d", cityParkingContract.TABLE_NAME,
                cityParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
