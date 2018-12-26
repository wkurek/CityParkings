package model.address;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.country.Country;
import model.country.CountryDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class AddressDAO {
    private static class AddressContract {
        static final String TABLE_NAME = "addresses";
        static final String COLUMN_NAME_ID = "address_id";
        static final String COLUMN_NAME_CITY = "city";
        static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        static final String COLUMN_NAME_STREET = "street";
        static final String COLUMN_NAME_NUMBER = "number";
        static final String COLUMN_NAME_COUNTRY = "country";
    }

    private static Address generateAddress(CachedRowSet resultSet) throws SQLException {
        Address address = new Address();

        Country country = CountryDAO.generateCountry(resultSet);
        address.setCountry(country);

        address.setId(resultSet.getInt(AddressContract.COLUMN_NAME_ID));
        address.setNumber(resultSet.getString(AddressContract.COLUMN_NAME_NUMBER));
        address.setStreet(resultSet.getString(AddressContract.COLUMN_NAME_STREET));
        address.setZipCode(resultSet.getString(AddressContract.COLUMN_NAME_ZIP_CODE));
        address.setCity(resultSet.getString(AddressContract.COLUMN_NAME_CITY));

        return address;
    }

    private static ObservableList<Address> generateAddressesList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Address> addressesList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Address address = generateAddress(resultSet);
            addressesList.add(address);
        }

        return addressesList;
    }

    public static ObservableList<Address> getAddresses() {
        //TODO: change the query for one with JOIN to fill the COUNTRY variable
        String sql = String.format("SELECT * FROM %s", AddressContract.TABLE_NAME);
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Address> addressesList = null;

        try {
            addressesList = generateAddressesList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return addressesList;
    }

    public static Address getAddress(int id) {
        //TODO: change the query for one with JOIN to fill the COUNTRY variable
        String sql = String.format("SELECT * FROM %s WHERE %s=%d", AddressContract.TABLE_NAME,
                AddressContract.COLUMN_NAME_ID, id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Address address = null;

        try {
            address = generateAddress(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return address;
    }

    public static void saveAddress(Address address) {
        if(address == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (0, '%s', '%s', '%s', '%s', '%s')", AddressContract.TABLE_NAME,
                address.getCity(), address.getZipCode(), address.getStreet(), address.getNumber(), address.getCountry());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateAddress(int id, Address updatedAddress) {
        if(updatedAddress == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s' WHERE %s = %d", AddressContract.TABLE_NAME,
                AddressContract.COLUMN_NAME_CITY, updatedAddress.getCity(),
                AddressContract.COLUMN_NAME_ZIP_CODE, updatedAddress.getZipCode(),
                AddressContract.COLUMN_NAME_STREET, updatedAddress.getStreet(),
                AddressContract.COLUMN_NAME_NUMBER, updatedAddress.getNumber(),
                AddressContract.COLUMN_NAME_COUNTRY, updatedAddress.getCountry().getName(),
                AddressContract.COLUMN_NAME_ID, updatedAddress.getId());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteAddress(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d", AddressContract.TABLE_NAME,
                AddressContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
