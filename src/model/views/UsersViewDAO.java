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
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SURNAME = "surname";
        static final String COLUMN_NAME_PHONE_NR = "phone_nr";
        static final String COLUMN_NAME_COUNTRY = "country";
        static final String COLUMN_NAME_CITY = "city";
        static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        static final String COLUMN_NAME_STREET = "street";
        static final String COLUMN_NAME_NUMBER= "number";
        static final String COLUMN_NAME_NUMBER_OF_VEHICLES = "nr_of_vehicles";
        static final String COLUMN_NAME_CARD_ID = "card_id";
        static final String COLUMN_NAME_EXPIRATION_DATE = "expiration_date";
    }

    private static int nrOfUsers;
    private static int summedVehicles;
    private static float averageVehicles;
    private static int withoutVehicle;

    public static ObservableList<UsersView> getUsersViews(String vehiclesMinInput, String vehiclesMaxInput,
                                                          String dateMinInput, String dateMaxInput, List<String> countries)
    {
        String sql = generateSelectWhereQuery(vehiclesMinInput, vehiclesMaxInput, dateMinInput, dateMaxInput, countries);

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<UsersView> usersViewsList = FXCollections.observableArrayList();
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
        if (vehiclesMinInput != null && !vehiclesMinInput.equals("") && Validator.isIntegerInputValid(vehiclesMinInput)) {
            sql+="and "+UsersViewContract.COLUMN_NAME_NUMBER_OF_VEHICLES+">="+vehiclesMinInput+" ";
        }
        if (vehiclesMaxInput != null && !vehiclesMaxInput.equals("") && Validator.isIntegerInputValid(vehiclesMaxInput)) {
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

    private static UsersView generateUsersView(CachedRowSet resultSet) throws SQLException {
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

    public static void generateStatistics(List<UsersView> usersViews)
    {
        nrOfUsers = usersViews.size();
        int vehicles=0;
        withoutVehicle=0;
        for(UsersView u : usersViews) {
            if(u.getNumberOfVehicles()==0)
                withoutVehicle++;
            else
                vehicles += u.getNumberOfVehicles();
        }
        summedVehicles = vehicles;
        if(nrOfUsers==0)
            averageVehicles=0;
        else
            averageVehicles = (float)summedVehicles/(float)nrOfUsers;

    }

    public static int getNrOfUsers() {
        return nrOfUsers;
    }

    public static int getSummedVehicles() {
        return summedVehicles;
    }

    public static float getAverageVehicles() {
        return averageVehicles;
    }

    public static int getWithoutVehicle() {
        return withoutVehicle;
    }
}
