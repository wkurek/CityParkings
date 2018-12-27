package model.park;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.address.AddressDAO;
import model.card.CardDAO;
import model.country.CountryDAO;
import model.location.LocationDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import model.user.UserDAO;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class ParkDAO {
    public static class ParkContract {
        public static final String TABLE_NAME = "[dbo].[parks]";
        public static final String COLUMN_NAME_VEHICLE_ID = "vehicle_id";
        public static final String COLUMN_NAME_PARKING_ID = "parking_id";
        public static final String COLUMN_NAME_DATE_TIME = "date_time";
    }


    private static String generateSelectQuery() {
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s," +
                " %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s," +
                        "%s.%s, %s.%s, %s.%s, %s.%s " +
                        "FROM %s LEFT JOIN %s ON %s.%s=%s.%s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s",
                ParkContract.TABLE_NAME, ParkContract.COLUMN_NAME_DATE_TIME,

                VehicleDAO.VehicleContract.TABLE_NAME, VehicleDAO.VehicleContract.COLUMN_NAME_ID,
                VehicleDAO.VehicleContract.TABLE_NAME, VehicleDAO.VehicleContract.COLUMN_NAME_PLATE_NUMBER,
                VehicleDAO.VehicleContract.TABLE_NAME, VehicleDAO.VehicleContract.COLUMN_NAME_WEIGHT,
                VehicleDAO.VehicleContract.TABLE_NAME, VehicleDAO.VehicleContract.COLUMN_NAME_HEIGHT,
                VehicleDAO.VehicleContract.TABLE_NAME, VehicleDAO.VehicleContract.COLUMN_NAME_ENGINE_TYPE,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_ID,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_NAME,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_SURNAME,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_PHONE_NUMBER,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_COUNTRY,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_ISO,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ID,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_CITY,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ZIP_CODE,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_STREET,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_NUMBER,
                CardDAO.CardContract.TABLE_NAME, CardDAO.CardContract.COLUMN_NAME_ID,
                CardDAO.CardContract.TABLE_NAME, CardDAO.CardContract.COLUMN_NAME_EXPIRATION_DATE,

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

                ParkContract.TABLE_NAME, VehicleDAO.VehicleContract.TABLE_NAME,
                ParkContract.TABLE_NAME, ParkContract.COLUMN_NAME_VEHICLE_ID,
                VehicleDAO.VehicleContract.TABLE_NAME, VehicleDAO.VehicleContract.COLUMN_NAME_ID,

                UserDAO.UserContract.TABLE_NAME,
                VehicleDAO.VehicleContract.TABLE_NAME, VehicleDAO.VehicleContract.COLUMN_NAME_USER_ID,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_ID,

                CardDAO.CardContract.TABLE_NAME,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_CARD_ID,
                CardDAO.CardContract.TABLE_NAME, CardDAO.CardContract.COLUMN_NAME_ID,

                AddressDAO.AddressContract.TABLE_NAME,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_ADDRESS_ID,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ID,

                CountryDAO.CountryContract.TABLE_NAME,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_COUNTRY,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_COUNTRY,

                ParkingDAO.ParkingContract.TABLE_NAME,
                ParkContract.TABLE_NAME, ParkContract.COLUMN_NAME_PARKING_ID,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_ID,

                LocationDAO.LocationContract.TABLE_NAME,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_LOCATION_ID,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID
                );
    }

    public static Park generatePark(CachedRowSet resultSet) throws SQLException {
        Park park = new Park();

        Vehicle vehicle = new Vehicle();
        park.setVehicle(vehicle);

        Parking parking = ParkingDAO.generateParking(resultSet);
        park.setParking(parking);

        return park;
    }

    private static ObservableList<Park> generateParkList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Park> parkList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Park park = generatePark(resultSet);
            parkList.add(park);
        }

        return parkList;
    }

    public static ObservableList<Park> getParks() {
        String sql = generateSelectQuery();
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Park> parkList = null;

        try {
            parkList = generateParkList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return parkList;
    }

    public static Park getPark(String date, int id) {
        String sql = String.format("%s WHERE %s.%s=%d AND %s.%s='%s'", generateSelectQuery(),
                ParkContract.TABLE_NAME, ParkContract.COLUMN_NAME_VEHICLE_ID, id,
                ParkContract.TABLE_NAME, ParkContract.COLUMN_NAME_DATE_TIME, date);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Park park = null;

        try {
            park = generatePark(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return park;
    }

    public static void savePark(Park park) {
        if(park == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (%d, %d, '%s')", ParkDAO.ParkContract.TABLE_NAME,
                park.getVehicle().getId(), park.getParking().getParkingId(), park.getDateTime());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updatePark(int id, Park updatedPark) {
        if(updatedPark == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = %d WHERE %s = %d AND %s = '%s'", ParkContract.TABLE_NAME,
                ParkContract.COLUMN_NAME_PARKING_ID, updatedPark.getParking().getParkingId(),
                ParkContract.COLUMN_NAME_VEHICLE_ID, updatedPark.getVehicle().getId(),
                ParkContract.COLUMN_NAME_DATE_TIME, updatedPark.getDateTime());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deletePark(String date, int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d AND %s = '%s'", ParkDAO.ParkContract.TABLE_NAME,
                ParkContract.COLUMN_NAME_VEHICLE_ID, id,
                ParkContract.COLUMN_NAME_DATE_TIME, date);

        DbHelper.executeUpdateQuery(sql);
    }
}
