package model.country;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class CountryDAO {
    public static class CountryContract {
        public static final String TABLE_NAME = "[dbo].[countries]";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_ISO = "iso";
    }

    public static Country generateCountry(CachedRowSet resultSet) throws SQLException {
        Country country = new Country();
        country.setName(resultSet.getString(CountryContract.COLUMN_NAME_COUNTRY));
        country.setIso(resultSet.getString(CountryContract.COLUMN_NAME_ISO));

        return country;
    }

    private static ObservableList<Country> generateCountryList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Country country = generateCountry(resultSet);
            countryList.add(country);
        }

        return countryList;
    }

    public static ObservableList<Country> getCountries() {
        String sql = String.format("SELECT * FROM %s", CountryContract.TABLE_NAME);
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Country> countryList = null;

        try {
            countryList = generateCountryList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return countryList;
    }

    public static Country getCountry(String countryName) {
        String sql = String.format("SELECT * FROM %s WHERE %s=%s", CountryContract.TABLE_NAME,
                CountryContract.COLUMN_NAME_COUNTRY, countryName);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Country country = null;

        try {
            country = generateCountry(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return country;
    }

    public static void saveCountry(Country country) {
        if(country == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES ('%s', '%s')", CountryContract.TABLE_NAME, country.getName(),
                country.getIso());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateCountry(String countryName, Country updatedCountry) {
        if(updatedCountry == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s' WHERE %s = %s", CountryContract.TABLE_NAME,
                CountryContract.COLUMN_NAME_ISO, updatedCountry.getIso(),
                CountryContract.COLUMN_NAME_COUNTRY, updatedCountry.getName());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteCountry(String countryName) {
        String sql = String.format("DELETE FROM %s WHERE %s = %s", CountryContract.TABLE_NAME,
                CountryContract.COLUMN_NAME_COUNTRY, countryName);

        DbHelper.executeUpdateQuery(sql);
    }

}
