package model.park;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.parking.Parking;
import model.parking.ParkingDAO;
import model.vehicle.Vehicle;
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
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s" +
                " FROM %s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s",
                ParkContract.TABLE_NAME, ParkContract.COLUMN_NAME_DATE_TIME,
















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
