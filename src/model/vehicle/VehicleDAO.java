package model.vehicle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.address.AddressDAO;
import model.card.CardDAO;
import model.country.CountryDAO;
import model.engine.Engine;
import model.engine.EngineDAO;
import model.user.User;
import model.user.UserDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.Locale;

public class VehicleDAO {
    public static class VehicleContract {
        public static final String TABLE_NAME = "[dbo].[vehicles]";
        public static final String COLUMN_NAME_ID = "vehicle_id";
        public static final String COLUMN_NAME_PLATE_NUMBER = "plate_number";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_ENGINE_TYPE = "engine_type";
        public static final String COLUMN_NAME_USER_ID= "user_id";
    }

    public static Vehicle generateVehicle(CachedRowSet resultSet) throws SQLException {
        Vehicle vehicle = new Vehicle();

        User user = UserDAO.generateUser(resultSet);
        Engine engine = EngineDAO.generateEngine(resultSet);

        vehicle.setUser(user);
        vehicle.setEngine(engine);

        vehicle.setId(resultSet.getInt(VehicleContract.COLUMN_NAME_ID));
        vehicle.setPlateNumber(resultSet.getString(VehicleContract.COLUMN_NAME_PLATE_NUMBER));
        vehicle.setWeight(resultSet.getFloat(VehicleContract.COLUMN_NAME_WEIGHT));
        vehicle.setHeight(resultSet.getFloat(VehicleContract.COLUMN_NAME_HEIGHT));

        return vehicle;
    }

    private static ObservableList<Vehicle> generateVehiclesList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Vehicle> vehiclesList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Vehicle vehicle = generateVehicle(resultSet);
            vehiclesList.add(vehicle);
        }

        return vehiclesList;
    }


    private static String generateSelectQuery() {
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, " +
                        "%s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s FROM %s LEFT JOIN %s ON %s.%s=%s.%s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s",
                VehicleContract.TABLE_NAME, VehicleContract.COLUMN_NAME_ID,
                VehicleContract.TABLE_NAME, VehicleContract.COLUMN_NAME_PLATE_NUMBER,
                VehicleContract.TABLE_NAME, VehicleContract.COLUMN_NAME_WEIGHT,
                VehicleContract.TABLE_NAME, VehicleContract.COLUMN_NAME_HEIGHT,
                VehicleContract.TABLE_NAME, VehicleContract.COLUMN_NAME_ENGINE_TYPE,
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

                VehicleContract.TABLE_NAME, UserDAO.UserContract.TABLE_NAME,
                VehicleContract.TABLE_NAME, VehicleContract.COLUMN_NAME_USER_ID,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_ID,

                CardDAO.CardContract.TABLE_NAME,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_CARD_ID,
                CardDAO.CardContract.TABLE_NAME, CardDAO.CardContract.COLUMN_NAME_ID,

                AddressDAO.AddressContract.TABLE_NAME,
                UserDAO.UserContract.TABLE_NAME, UserDAO.UserContract.COLUMN_NAME_ADDRESS_ID,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ID,

                CountryDAO.CountryContract.TABLE_NAME,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_COUNTRY,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_COUNTRY);

    }

    private static String generateSelectWhereQuery(int id) {
        String sql = generateSelectQuery();
        return String.format("%s WHERE %s.%s=%d", sql, VehicleContract.TABLE_NAME, VehicleContract.COLUMN_NAME_ID, id);
    }

    public static ObservableList<Vehicle> getVehicles() {
        String sql = generateSelectQuery();

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Vehicle> vehiclesList = null;

        try {
            vehiclesList = generateVehiclesList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return vehiclesList;
    }

    public static ObservableList<Vehicle> getUserVehicles(int userId) {
        String sql = String.format("%s WHERE %s.%s=%d", generateSelectQuery(),
                VehicleContract.TABLE_NAME, VehicleContract.COLUMN_NAME_USER_ID, userId);

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Vehicle> vehiclesList = null;

        try {
            vehiclesList = generateVehiclesList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return vehiclesList;
    }

    public static Vehicle getVehicle(int id) {
        String sql = generateSelectWhereQuery(id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Vehicle vehicle = null;

        try {
            vehicle = generateVehicle(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return vehicle;
    }

    public static void saveVehicle(Vehicle vehicle) {
        if(vehicle == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format(Locale.US, "INSERT INTO %s VALUES ('%s', %.2f, %.2f, '%s', %d)", VehicleContract.TABLE_NAME,
                vehicle.getPlateNumber(), vehicle.getWeight(), vehicle.getHeight(), vehicle.getEngine().getType(),
                vehicle.getUser().getId());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateVehicle(int id, Vehicle updatedVehicle) {
        if(updatedVehicle == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s', %s = %.2f, %s = %.2f, %s = '%s', %s = %d WHERE %s = %d",
                VehicleContract.TABLE_NAME,
                VehicleContract.COLUMN_NAME_PLATE_NUMBER, updatedVehicle.getPlateNumber(),
                VehicleContract.COLUMN_NAME_WEIGHT, updatedVehicle.getWeight(),
                VehicleContract.COLUMN_NAME_HEIGHT, updatedVehicle.getHeight(),
                VehicleContract.COLUMN_NAME_ENGINE_TYPE, updatedVehicle.getEngine().getType(),
                VehicleContract.COLUMN_NAME_USER_ID, updatedVehicle.getUser().getId(),
                VehicleContract.COLUMN_NAME_ID, updatedVehicle.getId());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteVehicle(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d",VehicleContract.TABLE_NAME,
                VehicleContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

}
