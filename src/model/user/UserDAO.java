package model.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.address.Address;
import model.address.AddressDAO;
import model.card.Card;
import model.card.CardDAO;
import model.country.CountryDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class UserDAO {
    public static class UserContract {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_ID = "user_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_nr";
        public static final String COLUMN_NAME_CARD_ID = "card_id";
        public static final String COLUMN_NAME_ADDRESS_ID = "address_id";
    }

    public static User generateUser(CachedRowSet resultSet) throws SQLException {
        User user = new User();

        Address address = AddressDAO.generateAddress(resultSet);
        Card card = CardDAO.generateCard(resultSet);

        user.setAddress(address);
        user.setCard(card);

        user.setId(resultSet.getInt(UserContract.COLUMN_NAME_ID));
        user.setName(resultSet.getString(UserContract.COLUMN_NAME_NAME));
        user.setSurname(resultSet.getString(UserContract.COLUMN_NAME_SURNAME));
        user.setPhoneNumber(resultSet.getString(UserContract.COLUMN_NAME_PHONE_NUMBER));

        return user;
    }

    private static ObservableList<User> generateUsersList(CachedRowSet resultSet) throws SQLException {
        ObservableList<User> usersList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            User user = generateUser(resultSet);
            usersList.add(user);
        }

        return usersList;
    }


    private static String generateSelectQuery() {
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, " +
                        "%s.%s, %s.%s, %s.%s, %s.%s FROM %s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s",
                UserContract.TABLE_NAME, UserContract.COLUMN_NAME_ID,
                UserContract.TABLE_NAME, UserContract.COLUMN_NAME_NAME,
                UserContract.TABLE_NAME, UserContract.COLUMN_NAME_SURNAME,
                UserContract.TABLE_NAME, UserContract.COLUMN_NAME_PHONE_NUMBER,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_COUNTRY,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_ISO,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ID,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_CITY,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ZIP_CODE,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_STREET,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_NUMBER,
                CardDAO.CardContract.TABLE_NAME, CardDAO.CardContract.COLUMN_NAME_ID,
                CardDAO.CardContract.TABLE_NAME, CardDAO.CardContract.COLUMN_NAME_EXPIRATION_DATE,

                UserContract.TABLE_NAME, CardDAO.CardContract.TABLE_NAME,
                UserContract.TABLE_NAME, UserContract.COLUMN_NAME_CARD_ID,
                CardDAO.CardContract.TABLE_NAME, CardDAO.CardContract.COLUMN_NAME_ID,

                AddressDAO.AddressContract.TABLE_NAME,
                UserContract.TABLE_NAME, UserContract.COLUMN_NAME_ADDRESS_ID,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ID,

                CountryDAO.CountryContract.TABLE_NAME,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_COUNTRY,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_COUNTRY);

    }

    private static String generateSelectWhereQuery(int id) {
        String sql = generateSelectQuery();
        return String.format("%s WHERE %s.%s=%d", sql, UserContract.TABLE_NAME, UserContract.COLUMN_NAME_ID, id);
    }

    public static ObservableList<User> getUsers() {
        String sql = generateSelectQuery();

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<User> usersList = null;

        try {
            usersList = generateUsersList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return usersList;
    }

    public static User getUser(int id) {
        String sql = generateSelectWhereQuery(id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        User user = null;

        try {
            user = generateUser(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return user;
    }

    public static void saveUser(User user) {
        if(user == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (0, '%s', '%s', '%s', %d, %d)", UserContract.TABLE_NAME,
                user.getName(), user.getSurname(), user.getPhoneNumber(), user.getCard().getCardId(),
                user.getAddress().getId());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateUser(int id, User updatedUser) {
        if(updatedUser == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s', %s = '%s', %s = '%s', %s = %d, %s = %d WHERE %s = %d",
                UserContract.TABLE_NAME,
                UserContract.COLUMN_NAME_NAME, updatedUser.getName(),
                UserContract.COLUMN_NAME_SURNAME, updatedUser.getSurname(),
                UserContract.COLUMN_NAME_PHONE_NUMBER, updatedUser.getPhoneNumber(),
                UserContract.COLUMN_NAME_ADDRESS_ID, updatedUser.getAddress().getId(),
                UserContract.COLUMN_NAME_CARD_ID, updatedUser.getCard().getCardId(),
                UserContract.COLUMN_NAME_ID, updatedUser.getId());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteUser(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d",UserContract.TABLE_NAME,
               UserContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
