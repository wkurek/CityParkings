package model.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;
import util.Validator;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehiclesViewDAO {
    private static class VehiclesViewContract {
        public static final String TABLE_NAME = "[dbo].[vehicles_view]";
        static final String PARKS_TABLE_NAME = "[dbo].[parks]";
        static final String PARKINGS_TABLE_NAME = "[dbo].[parkings]";
        static final String CITY_TABLE_NAME = "[dbo].[city_parkings]";
        static final String PARK_RIDES_TABLE_NAME = "[dbo].[park_rides]";
        static final String KISS_RIDES_TABLE_NAME = "[dbo].[kiss_rides]";
        static final String ESTATES_TABLE_NAME = "[dbo].[estate_parkings]";
        static final String PARKINGS_COLUMN_NAME_ID = "parking_id";
        public static final String COLUMN_NAME_ID = "vehicle_id";
        static final String COLUMN_NAME_PLATE_NUMBER = "plate_number";
        static final String COLUMN_NAME_HEIGHT = "height";
        static final String COLUMN_NAME_WEIGHT = "weight";
        static final String COLUMN_NAME_ENGINE_TYPE = "engine_type";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SURNAME = "surname";
        static final String COLUMN_NAME_PHONE_NUMBER = "phone_nr";
        static final String COLUMN_NAME_COUNTRY = "country";
        static final String COLUMN_NAME_CITY = "city";
        static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        static final String COLUMN_NAME_STREET = "street";
        static final String COLUMN_NAME_NUMBER= "number";
        static final String COLUMN_NAME_PARKING_ID = "parking_id";
        static final String COLUMN_NAME_DATE_TIME = "date_time";
    }

    private static int nrOfVehicles;
    private static int nrOfVehiclesParked;
    private static int onCityParkings;
    private static int onParkRides;
    private static int onKissRides;
    private static int onEstateParkings;

    private static Map<String, Integer> engineTypesAndNumber = new HashMap<>();

    public static ObservableList<VehiclesView> getVehiclesViews(String heightMinInput, String heightMaxInput, String weightMinInput, String weightMaxInput,
                                                                List<String> countries, List<String> engines, boolean isParked)
    {
        String sql = generateSelectQuery(isParked) + generateWherePartOfQuery(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput,
                                                countries, engines);


        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<VehiclesView> vehiclesViewsList = FXCollections.observableArrayList();
        try {
            vehiclesViewsList = generateVehiclesViewsList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return vehiclesViewsList;
    }

    private static String generateWherePartOfQuery(String heightMinInput, String heightMaxInput, String weightMinInput, String weightMaxInput,
                                                   List<String> countries, List<String> engines)
    {
        String sql=" WHERE 1=1 ";
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
    private static String generateSelectQuery(boolean isParked)
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
            sql="SELECT * FROM " + VehiclesViewContract.TABLE_NAME;
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

    private static VehiclesView generateVehiclesView(CachedRowSet resultSet) throws SQLException {
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

    public static void generateStatistics(String heightMinInput, String heightMaxInput, String weightMinInput, String weightMaxInput,
                                   List<String> countries, List<String> engines, boolean isParked)
    {

        generateEngineTypesAndNumber(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput, countries, engines, isParked);
        nrOfVehicles = getVehiclesViews(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput, countries, engines, isParked).size();
        if(!isParked)
             nrOfVehiclesParked = getVehiclesViews(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput, countries, engines, true).size();
        else
            nrOfVehiclesParked=nrOfVehicles;
        onCityParkings = getParksOnOtherParkings(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput, countries, engines, VehiclesViewContract.CITY_TABLE_NAME);
        onParkRides = getParksOnOtherParkings(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput, countries, engines, VehiclesViewContract.PARK_RIDES_TABLE_NAME);
        onKissRides = getParksOnOtherParkings(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput, countries, engines, VehiclesViewContract.KISS_RIDES_TABLE_NAME);
        onEstateParkings = getParksOnOtherParkings(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput, countries, engines, VehiclesViewContract.ESTATES_TABLE_NAME);


    }

    private static int getParksOnOtherParkings(String heightMinInput, String heightMaxInput, String weightMinInput,
                                                     String weightMaxInput, List<String> countries, List<String> engines,
                                                     String tableName) {
        String sql = "SELECT COUNT(*) AS x FROM "+VehiclesViewContract.TABLE_NAME+" INNER JOIN "+VehiclesViewContract.PARKS_TABLE_NAME+
                " ON "+VehiclesViewContract.TABLE_NAME+"."+VehiclesViewContract.COLUMN_NAME_ID+"="+
                VehiclesViewContract.PARKS_TABLE_NAME+"."+VehiclesViewContract.COLUMN_NAME_ID+
                " INNER JOIN "+VehiclesViewContract.PARKINGS_TABLE_NAME+" ON "+VehiclesViewContract.PARKINGS_TABLE_NAME+"."+
                VehiclesViewContract.PARKINGS_COLUMN_NAME_ID+"="+VehiclesViewContract.PARKS_TABLE_NAME+"."+VehiclesViewContract.PARKINGS_COLUMN_NAME_ID+
                " INNER JOIN "+tableName+" ON "+VehiclesViewContract.PARKINGS_TABLE_NAME+"."+VehiclesViewContract.PARKINGS_COLUMN_NAME_ID+
                "="+tableName+"."+VehiclesViewContract.PARKINGS_COLUMN_NAME_ID+generateWherePartOfQuery(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput, countries, engines);

        CachedRowSet resultSet = DbHelper.executeQuery(sql);
        try
        {
            if(resultSet.next())
                return resultSet.getInt("x");
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    private static void generateEngineTypesAndNumber(String heightMinInput, String heightMaxInput, String weightMinInput, String weightMaxInput,
                                                     List<String> countries, List<String> engines, boolean isParked) {
        String sql = "SELECT COUNT("+VehiclesViewContract.COLUMN_NAME_ENGINE_TYPE+") as engine_count, "+VehiclesViewContract.COLUMN_NAME_ENGINE_TYPE+
                " FROM "+VehiclesViewContract.TABLE_NAME;
        if(isParked)
        {
            sql +=" INNER JOIN "+VehiclesViewContract.PARKS_TABLE_NAME+" ON "+VehiclesViewContract.TABLE_NAME+
                    "."+VehiclesViewContract.COLUMN_NAME_ID+"="+VehiclesViewContract.PARKS_TABLE_NAME+"."+VehiclesViewContract.COLUMN_NAME_ID;
        }
        sql+=generateWherePartOfQuery(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput, countries, engines)+
                " GROUP BY "+VehiclesViewContract.COLUMN_NAME_ENGINE_TYPE;
        CachedRowSet resultSet = DbHelper.executeQuery(sql);
        engineTypesAndNumber.clear();
        try {
            while (resultSet.next()) {
                  engineTypesAndNumber.put(resultSet.getString(VehiclesViewContract.COLUMN_NAME_ENGINE_TYPE), resultSet.getInt("engine_count"));
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public static Map<String, Integer> getEngineTypesAndNumber()
    {
        return engineTypesAndNumber;
    }
    public static int getNrOfVehicles() {
        return nrOfVehicles;
    }

    public static int getNrOfVehiclesParked() {
        return nrOfVehiclesParked;
    }

    public static int getOnCityParkings() {
        return onCityParkings;
    }

    public static int getOnParkRides() {
        return onParkRides;
    }

    public static int getOnKissRides() {
        return onKissRides;
    }

    public static int getOnEstateParkings() {
        return onEstateParkings;
    }
}
