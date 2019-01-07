package model.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;
import util.Validator;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.List;

public class VehiclesViewDAO {
    private static class VehiclesViewContract {
        public static final String TABLE_NAME = "[dbo].[vehicles_view]";
        public static final String PARKS_TABLE_NAME = "[dbo].[parks]";
        public static final String COLUMN_NAME_ID = "vehicle_id";
        public static final String COLUMN_NAME_PLATE_NUMBER = "plate_number";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_ENGINE_TYPE = "engine_type";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_nr";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_NUMBER= "number";
        public static final String COLUMN_NAME_PARKING_ID = "parking_id";
        public static final String COLUMN_NAME_DATE_TIME = "date_time";
    }

    public static ObservableList<VehiclesView> getVehiclesViews(String heightMinInput, String heightMaxInput, String weightMinInput, String weightMaxInput,
                                                                List<String> countries, List<String> engines, boolean isParked)
    {
        String sql = generateSelectWhereQuery(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput,
                                                countries, engines, isParked);


        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<VehiclesView> vehiclesViewsList = null;
        try {
            vehiclesViewsList = generateVehiclesViewsList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return vehiclesViewsList;
    }

    private static String generateSelectWhereQuery(String heightMinInput, String heightMaxInput, String weightMinInput, String weightMaxInput,
                                                   List<String> countries, List<String> engines, boolean isParked)
    {
        String sql;
        if(isParked)
        {
            sql = "SELECT "+VehiclesViewContract.TABLE_NAME+".* FROM "+VehiclesViewContract.TABLE_NAME+
                    " INNER JOIN "+VehiclesViewContract.PARKS_TABLE_NAME+" ON "+VehiclesViewContract.TABLE_NAME+
                    "."+VehiclesViewContract.COLUMN_NAME_ID+"="+VehiclesViewContract.PARKS_TABLE_NAME+"."+VehiclesViewContract.COLUMN_NAME_ID;
        }
        else
        {
            sql=generateSelectQuery();
        }
        sql+=" WHERE 1=1 ";
        if(heightMinInput!=null && Validator.isHeightValid(heightMinInput)){
            sql+="and "+VehiclesViewContract.COLUMN_NAME_HEIGHT+">="+heightMinInput+" ";
        }
        if(heightMaxInput!=null && Validator.isHeightValid(heightMaxInput)){
            sql+="and "+VehiclesViewContract.COLUMN_NAME_HEIGHT+"<="+heightMaxInput+" ";
        }
        if(weightMinInput!=null && Validator.isWeightValid(weightMinInput)){
            sql+="and "+VehiclesViewContract.COLUMN_NAME_WEIGHT+">="+weightMinInput+" ";
        }
        if(weightMaxInput!=null && Validator.isWeightValid(weightMaxInput)){
            sql+="and "+VehiclesViewContract.COLUMN_NAME_WEIGHT+"<="+weightMaxInput+" ";
        }
        if(countries.size()!=0&&!countries.get(0).equals("Select All")) {
            sql+="and(";
            for(String s : countries) {
                sql+=VehiclesViewContract.COLUMN_NAME_COUNTRY+" = '"+s+"' or ";
            }
            sql+="1=0) ";
        }
        if(engines.size()!=0&&!engines.get(0).equals("Select All")) {
            sql+="and(";
            for(String s : engines) {
                sql+=VehiclesViewContract.COLUMN_NAME_ENGINE_TYPE+" = '"+s+"' or ";
            }
            sql+="1=0)";
        }
        return sql;
    }
    private static ObservableList<VehiclesView> generateVehiclesViewsList(CachedRowSet resultSet) throws SQLException{
        ObservableList<VehiclesView> vehiclesViewsList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            VehiclesView vehiclesView = generateVehiclesView(resultSet);
            vehiclesViewsList.add(vehiclesView);
        }

        return vehiclesViewsList;
    }
    private static String generateSelectQuery()
    {
        return "SELECT * FROM " + VehiclesViewContract.TABLE_NAME;
    }

    public static VehiclesView generateVehiclesView(CachedRowSet resultSet) throws SQLException {
        VehiclesView vehiclesView = new VehiclesView();

        vehiclesView.setId(resultSet.getInt(VehiclesViewContract.COLUMN_NAME_ID));
        vehiclesView.setPlateNumber(resultSet.getString(VehiclesViewContract.COLUMN_NAME_PLATE_NUMBER));
        vehiclesView.setHeight(resultSet.getFloat(VehiclesViewContract.COLUMN_NAME_HEIGHT));
        vehiclesView.setWeight(resultSet.getFloat(VehiclesViewContract.COLUMN_NAME_WEIGHT));
        vehiclesView.setEngineType(resultSet.getString(VehiclesViewContract.COLUMN_NAME_ENGINE_TYPE));
        vehiclesView.setOwnerName(resultSet.getString(VehiclesViewContract.COLUMN_NAME_NAME));
        vehiclesView.setOwnerSurname(resultSet.getString(VehiclesViewContract.COLUMN_NAME_SURNAME));
        vehiclesView.setPhoneNumber(resultSet.getString(VehiclesViewContract.COLUMN_NAME_PHONE_NUMBER));
        vehiclesView.setCountry(resultSet.getString(VehiclesViewContract.COLUMN_NAME_COUNTRY));
        vehiclesView.setCity(resultSet.getString(VehiclesViewContract.COLUMN_NAME_CITY));
        vehiclesView.setZip_code(resultSet.getString(VehiclesViewContract.COLUMN_NAME_ZIP_CODE));
        vehiclesView.setStreet(resultSet.getString(VehiclesViewContract.COLUMN_NAME_STREET));
        vehiclesView.setNumber(resultSet.getString(VehiclesViewContract.COLUMN_NAME_NUMBER));
        vehiclesView.setParkingID(resultSet.getInt(VehiclesViewContract.COLUMN_NAME_PARKING_ID));
        vehiclesView.setParkDateTime(resultSet.getDate(VehiclesViewContract.COLUMN_NAME_DATE_TIME));

        return vehiclesView;
    }
}
