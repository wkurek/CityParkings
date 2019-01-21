package model.estate_parking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.estate.Estate;
import model.estate.EstateDAO;
import model.location.LocationDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class EstateParkingDAO {
    private static class EstateParkingContract {
        static final String TABLE_NAME = "[dbo].[estate_parkings]";
        static final String COLUMN_NAME_ID = "parking_id";
        static final String COLUMN_NAME_ESTATE_NAME = "estate_name";
    }

    private static String generateSelectQuery() {
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s" +
                " FROM %s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s",
                EstateDAO.EstateContract.TABLE_NAME, EstateDAO.EstateContract.COLUMN_NAME_ESTATE_NAME,

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

                EstateParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.TABLE_NAME,
                EstateParkingContract.TABLE_NAME, EstateParkingContract.COLUMN_NAME_ID,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_ID,

                EstateDAO.EstateContract.TABLE_NAME,
                EstateParkingContract.TABLE_NAME, EstateParkingContract.COLUMN_NAME_ESTATE_NAME,
                EstateDAO.EstateContract.TABLE_NAME, EstateDAO.EstateContract.COLUMN_NAME_ESTATE_NAME,

                LocationDAO.LocationContract.TABLE_NAME,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_LOCATION_ID,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID
        );
    }

    public static EstateParking generateEstateParking(CachedRowSet resultSet) throws SQLException {
        EstateParking estateParking = new EstateParking();

        Estate estate = EstateDAO.generateEstate(resultSet);
        estateParking.setEstate(estate);

        Parking parking = ParkingDAO.generateParking(resultSet);
        estateParking.setParking(parking);

        return estateParking;
    }

    private static ObservableList<EstateParking> generateEstateParkingList(CachedRowSet resultSet) throws SQLException {
        ObservableList<EstateParking> estateParkingList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            EstateParking estateParking = generateEstateParking(resultSet);
            estateParkingList.add(estateParking);
        }

        return estateParkingList;
    }

    public static ObservableList<EstateParking> getEstateParkings() {
        String sql = generateSelectQuery();
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<EstateParking> estateParkingList = null;

        try {
            estateParkingList = generateEstateParkingList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return estateParkingList;
    }

    public static EstateParking getEstateParking(int id) {
        String sql = String.format("%s WHERE %s.%s=%d", generateSelectQuery(),
                EstateParkingContract.TABLE_NAME, EstateParkingContract.COLUMN_NAME_ID, id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        EstateParking estateParking = null;

        try {
            estateParking = generateEstateParking(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return estateParking;
    }

    public static void saveEstateParking(EstateParking estateParking) {
        if(estateParking == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (%d, '%s')", EstateParkingContract.TABLE_NAME, estateParking.getParking().getParkingId(),
                estateParking.getEstate().getEstateName());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateEstateParking(int id, EstateParking updatedEstateParking) {
        if(updatedEstateParking == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s' WHERE %s = %d", EstateParkingContract.TABLE_NAME,
                EstateParkingContract.COLUMN_NAME_ESTATE_NAME, updatedEstateParking.getEstate().getEstateName(),
                EstateParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteEstateParking(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d", EstateParkingDAO.EstateParkingContract.TABLE_NAME,
                EstateParkingContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
