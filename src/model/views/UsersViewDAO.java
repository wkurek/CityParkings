package model.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;
import util.Validator;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.List;

public class UsersViewDAO {
    private static class UsersViewContract {
        public static final String TABLE_NAME = "[dbo].[users_view]";
        public static final String COLUMN_NAME_ID = "user_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_PHONE_NR = "phone_nr";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_NUMBER= "number";
        public static final String COLUMN_NAME_NUMBER_OF_VEHICLES = "nr_of_vehicles";
        public static final String COLUMN_NAME_CARD_ID = "card_id";
        public static final String COLUMN_NAME_EXPIRATION_DATE = "expiration_date";
    }

    public static ObservableList<UsersView> getUsersViews(String vehiclesMinInput, String vehiclesMaxInput,
                                                          String dateMinInput, String dateMaxInput, List<String> countries)
    {
        String sql = generateSelectWhereQuery(vehiclesMinInput, vehiclesMaxInput, dateMinInput, dateMaxInput, countries);

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<UsersView> usersViewsList = null;
        try {
            usersViewsList = generateUsersViewsList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return usersViewsList;
    }
    private static String generateSelectWhereQuery(String vehiclesMinInput, String vehiclesMaxInput,
                                                   String dateMinInput, String dateMaxInput, List<String> countries)
    {
        String sql = generateSelectQuery();
        sql+=" WHERE 1=1 ";
        if(vehiclesMinInput!=null && Validator.isIntegerInputValid(vehiclesMinInput)){
            sql+="and "+UsersViewContract.COLUMN_NAME_NUMBER_OF_VEHICLES+">="+vehiclesMinInput+" ";
        }
        if(vehiclesMaxInput!=null && Validator.isIntegerInputValid(vehiclesMaxInput)){
            sql+="and "+UsersViewContract.COLUMN_NAME_NUMBER_OF_VEHICLES+"<="+vehiclesMaxInput+" ";
        }
        if(dateMinInput!=null){
            sql+="and "+UsersViewContract.COLUMN_NAME_EXPIRATION_DATE+">='"+dateMinInput+"' ";
        }
        if(dateMaxInput!=null){
            sql+="and "+UsersViewContract.COLUMN_NAME_EXPIRATION_DATE+"<='"+dateMaxInput+"' ";
        }
        if(countries.size()!=0&&!countries.get(0).equals("Select All")) {
            sql+="and(";
            for(String s : countries) {
                sql+=UsersViewContract.COLUMN_NAME_COUNTRY+" = '"+s+"' or ";
            }
            sql+="1=0) ";
        }
        return sql;
    }
    private static String generateSelectQuery()
    {
        return "SELECT * FROM "+UsersViewContract.TABLE_NAME;
    }
    private static ObservableList<UsersView> generateUsersViewsList(CachedRowSet resultSet) throws SQLException {
        ObservableList<UsersView> usersViewsList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            UsersView usersView = generateUsersView(resultSet);
            usersViewsList.add(usersView);
        }

        return usersViewsList;
    }

    public static UsersView generateUsersView(CachedRowSet resultSet) throws SQLException {
        UsersView usersView = new UsersView();

        usersView.setId(resultSet.getInt(UsersViewContract.COLUMN_NAME_ID));
        usersView.setName(resultSet.getString(UsersViewContract.COLUMN_NAME_NAME));
        usersView.setSurname(resultSet.getString(UsersViewContract.COLUMN_NAME_SURNAME));
        usersView.setPhoneNumber(resultSet.getString(UsersViewContract.COLUMN_NAME_PHONE_NR));
        usersView.setCountry(resultSet.getString(UsersViewContract.COLUMN_NAME_COUNTRY));
        usersView.setCity(resultSet.getString(UsersViewContract.COLUMN_NAME_CITY));
        usersView.setZip_code(resultSet.getString(UsersViewContract.COLUMN_NAME_ZIP_CODE));
        usersView.setStreet(resultSet.getString(UsersViewContract.COLUMN_NAME_STREET));
        usersView.setNumber(resultSet.getString(UsersViewContract.COLUMN_NAME_NUMBER));
        usersView.setNumberOfVehicles(resultSet.getInt(UsersViewContract.COLUMN_NAME_NUMBER_OF_VEHICLES));
        usersView.setCardID(resultSet.getInt(UsersViewContract.COLUMN_NAME_CARD_ID));
        usersView.setExpirationDate(resultSet.getDate(UsersViewContract.COLUMN_NAME_EXPIRATION_DATE));

        return usersView;
    }

}
