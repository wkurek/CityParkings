package model.parking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.employee.EmployeeDAO;
import model.location.Location;
import model.location.LocationDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class ParkingDAO {
    public static class ParkingContract {
        public static final String TABLE_NAME = "[dbo].[parkings]";
        public static final String COLUMN_NAME_ID = "parking_id";
        public static final String COLUMN_NAME_STANDARD_LOTS = "standard_lots";
        public static final String COLUMN_NAME_DISABLED_LOTS = "disabled_lots";
        public static final String COLUMN_NAME_IS_ROOF = "is_roof";
        public static final String COLUMN_NAME_IS_GUARDED = "is_guarded";
        public static final String COLUMN_NAME_LAST_CONTROL = "last_control";
        public static final String COLUMN_NAME_WEIGHT = "max_weight";
        public static final String COLUMN_NAME_HEIGHT = "max_height";
        public static final String COLUMN_NAME_LOCATION_ID = "location_id";
    }

    public static Parking generateParking(CachedRowSet resultSet) throws SQLException {
        Parking parking = new Parking();

        Location location = LocationDAO.generateLocation(resultSet);
        parking.setLocation(location);

        parking.setParkingId(resultSet.getInt(ParkingContract.COLUMN_NAME_ID));
        parking.setStandardLots(resultSet.getInt(ParkingContract.COLUMN_NAME_STANDARD_LOTS));
        parking.setDisabledLots(resultSet.getInt(ParkingContract.COLUMN_NAME_DISABLED_LOTS));
        parking.setRoofed(resultSet.getBoolean(ParkingContract.COLUMN_NAME_IS_ROOF));
        parking.setGuarded(resultSet.getBoolean(ParkingContract.COLUMN_NAME_IS_GUARDED));
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
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s" +
                        " FROM %s LEFT JOIN %s ON %s.%s=%s.%s",
                    ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_ID,
                    ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_STANDARD_LOTS,
                    ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_DISABLED_LOTS,
                    ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_IS_ROOF,
                    ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_IS_GUARDED,
                    ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_LAST_CONTROL,
                    ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_WEIGHT,
                    ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_HEIGHT,

                    LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID,
                    LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_LATITUDE,
                    LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_LONGITUDE,

                    ParkingContract.TABLE_NAME, LocationDAO.LocationContract.TABLE_NAME,
                    ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_LOCATION_ID,
                    LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID
                );
    }

    private static String generateSelectWhereQuery(int id) {
        String sql = generateSelectQuery();
        return String.format("%s WHERE %s.%s=%d", sql, ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_ID, id);
    }

    private static String generateSelectNoEmployeeQuery(){
        String sql = generateSelectQuery();
        return String.format("%s LEFT JOIN %s ON %s.%s=%s.%s WHERE %s.%s IS NULL",
                sql, EmployeeDAO.EmployeeContract.TABLE_NAME,
                EmployeeDAO.EmployeeContract.TABLE_NAME, EmployeeDAO.EmployeeContract.COLUMN_NAME_PARKING_ID,
                ParkingContract.TABLE_NAME, ParkingContract.COLUMN_NAME_ID,
                EmployeeDAO.EmployeeContract.TABLE_NAME, EmployeeDAO.EmployeeContract.COLUMN_NAME_PARKING_ID);
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

    public static ObservableList<Parking> getNoEmployeeParkings() {
        String sql = generateSelectNoEmployeeQuery();

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Parking> parkingList = null;

        try {
            parkingList = generateParkingsList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return parkingList;
    }

    public static void saveParking(Parking parking) {
        if(parking == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (0, %d, %d, %b, %b, '%s', %.2f, %.2f, %d)",
                ParkingContract.TABLE_NAME, parking.getStandardLots(), parking.getDisabledLots(),
                parking.isRoofed(), parking.isGuarded(), parking.getLastControl(),
                parking.getMaxWeight(), parking.getMaxHeight(), parking.getLocation().getLocationId());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateParking(int id, Parking updatedParking) {
        if (updatedParking == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = %d, %s = %d, %s = %b, %s = %b, %s = '%s', %s = %.2f, " +
                        "%s = %.2f, %s = %d WHERE %s = %d",
                ParkingContract.TABLE_NAME,
                ParkingContract.COLUMN_NAME_STANDARD_LOTS, updatedParking.getStandardLots(),
                ParkingContract.COLUMN_NAME_DISABLED_LOTS, updatedParking.getDisabledLots(),
                ParkingContract.COLUMN_NAME_IS_ROOF, updatedParking.isRoofed(),
                ParkingContract.COLUMN_NAME_IS_GUARDED, updatedParking.isGuarded(),
                ParkingContract.COLUMN_NAME_LAST_CONTROL, updatedParking.getLastControl(),
                ParkingContract.COLUMN_NAME_WEIGHT, updatedParking.getMaxWeight(),
                ParkingContract.COLUMN_NAME_HEIGHT, updatedParking.getMaxHeight(),
                ParkingContract.COLUMN_NAME_LOCATION_ID, updatedParking.getLocation().getLocationId(),
                ParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteParking(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d",ParkingContract.TABLE_NAME,
                ParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

}
