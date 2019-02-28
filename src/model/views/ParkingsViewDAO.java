package model.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import util.DbHelper;
import util.Validator;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import org.joda.time.DateTime;
//import org.joda.time.Duration;
//import org.joda.time.Interval;



public class ParkingsViewDAO {

    public static class ParkingsViewContract {
        public static final String TABLE_NAME = "[dbo].[parkings_view]";
        public static final String COLUMN_NAME_ID = "parking_id";
        static final String COLUMN_NAME_STANDARD_LOTS = "standard_lots";
        static final String COLUMN_NAME_DISABLED_LOTS = "disabled_lots";
        static final String COLUMN_NAME_OCCUPIED_LOTS = "occupied_lots";
        static final String COLUMN_NAME_IS_ROOF = "is_roof";
        static final String COLUMN_NAME_IS_GUARDED = "is_guarded";
        static final String COLUMN_NAME_LAST_CONTROL = "last_control";
        static final String COLUMN_NAME_MAX_WEIGHT = "max_weight";
        static final String COLUMN_NAME_MAX_HEIGHT = "max_height";
        static final String COLUMN_NAME_LOCATION_ID = "location_ID";
        static final String COLUMN_NAME_PARK_TYPE = "park_type";
        static final String COLUMN_NAME_IS_ZONE_PAID = "is_zone_paid";
        static final String COLUMN_NAME_ESTATE_NAME = "estate_name";
        static final String COLUMN_NAME_ARE_GATES = "are_gates";
        static final String COLUMN_NAME_MAX_STOP_MINUTES = "max_stop_minutes";
        static final String COLUMN_NAME_COMMUNICATION_NODE_ = "communication_node";
        static final String COLUMN_NAME_IS_AUTOMATIC = "is_automatic";
    }
    public static final List<String> PARKING_TYPES_TABLE_NAMES = new ArrayList<>(Arrays.asList(
            "city_parkings",
            "park_rides",
            "kiss_rides",
            "estate_parkings"
    ));

    private static int nrOfParkings;
    private static int nrCityParkings;
    private static int nrParkRides;
    private static int nrKissRides;
    private static int nrEstateParkings;
    private static int nrStandardSlots;
    private static int nrDisabledSlots;
    private static int nrOccupiedSlots;
    private static float avParkTime;
    private static float avOccupancy;
    private static float longestParkTime;
    private static float shortestParkTime;

    public static ObservableList<ParkingsView> getParkingsViews(String heightMinInput, String heightMaxInput, String weightMinInput,
                                                                String weightMaxInput, String lotsMinInput, String lotsMaxInput, List<String> parkingType,
                                                                boolean isRoofed, boolean isGuarded, boolean hasFreeLots)
    {
        String sql = generateSelectQuery(parkingType) + generateWherePartOfQuery(heightMinInput, heightMaxInput, weightMinInput, weightMaxInput,
                                                                                    lotsMinInput, lotsMaxInput, isRoofed, isGuarded, hasFreeLots);

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<ParkingsView> parkingsViewList = FXCollections.observableArrayList();
        try {
             parkingsViewList = generateParkingsViewsList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return parkingsViewList;
    }

    private static String generateSelectQuery(List<String> parkingType)
    {
        String sql;
        if(parkingType.size()!=0)
        {
            if(parkingType.size()==1)
            {
                sql="SELECT "+ParkingsViewContract.TABLE_NAME+".* FROM "+ParkingsViewContract.TABLE_NAME+
                        "INNER JOIN "+parkingType.get(0)+" ON "+ParkingsViewContract.TABLE_NAME+"."+
                        ParkingsViewContract.COLUMN_NAME_ID+"="+parkingType+"."+ParkingsViewContract.COLUMN_NAME_ID;
            }
            else {
                sql = "SELECT " + ParkingsViewContract.TABLE_NAME + ".* FROM " + parkingType.get(0);
                for (int i = 1; i < parkingType.size(); i++) {
                    sql += " full join "+parkingType.get(i) + " on " + parkingType.get(0) + "." + ParkingsViewContract.COLUMN_NAME_ID +
                            "=" + parkingType.get(i) + "." + ParkingsViewContract.COLUMN_NAME_ID;
                }
                sql+=" inner join "+ParkingsViewContract.TABLE_NAME+" on ";
                for(String s : parkingType)
                {
                    sql+=s+"."+ParkingsViewContract.COLUMN_NAME_ID+"="+ParkingsViewContract.TABLE_NAME+"."+ParkingsViewContract.COLUMN_NAME_ID+" or ";
                }
                sql+="1=0";
            }
        }
        else
        {
            sql = "SELECT "+ParkingsViewContract.TABLE_NAME+".* FROM " + ParkingsViewDAO.ParkingsViewContract.TABLE_NAME+" ";
        }

        return sql;
    }

    private static ObservableList<ParkingsView> generateParkingsViewsList(CachedRowSet resultSet) throws SQLException{
        ObservableList<ParkingsView> parkingsViewsList = FXCollections.observableArrayList();

        while (resultSet!=null&&resultSet.next()) {
            ParkingsView parkingsView = generateParkingsView(resultSet);
            parkingsViewsList.add(parkingsView);
        }

        return parkingsViewsList;
    }


    private static String generateWherePartOfQuery(String heightMinInput, String heightMaxInput, String weightMinInput,
                                                   String weightMaxInput, String lotsMinInput, String lotsMaxInput,
                                                   boolean isRoofed, boolean isGuarded, boolean hasFreeLots)
    {
        String sql=" WHERE 1=1 ";
        if (heightMinInput != null && !heightMinInput.equals("") && Validator.isHeightValid(heightMinInput)) {
            sql+="and "+ParkingsViewContract.COLUMN_NAME_MAX_HEIGHT+">="+heightMinInput+" ";
        }
        if (heightMaxInput != null && !heightMaxInput.equals("") && Validator.isHeightValid(heightMaxInput)) {
            sql+="and "+ParkingsViewContract.COLUMN_NAME_MAX_HEIGHT+"<="+heightMaxInput+" ";
        }
        if (weightMinInput != null && !weightMinInput.equals("") && Validator.isWeightValid(weightMinInput)) {
            sql+="and "+ParkingsViewContract.COLUMN_NAME_MAX_WEIGHT+">="+weightMinInput+" ";
        }
        if (weightMaxInput != null && !weightMaxInput.equals("") && Validator.isWeightValid(weightMaxInput)) {
            sql+="and "+ParkingsViewContract.COLUMN_NAME_MAX_WEIGHT+"<="+weightMaxInput+" ";
        }
        if (lotsMinInput != null && !lotsMinInput.equals("") && Validator.isIntegerInputValid(lotsMinInput)) {
            sql+="and "+ParkingsViewContract.COLUMN_NAME_STANDARD_LOTS+"+"+ParkingsViewContract.COLUMN_NAME_DISABLED_LOTS+">="+lotsMinInput+" ";
        }
        if (lotsMaxInput != null && !lotsMaxInput.equals("") && Validator.isIntegerInputValid(lotsMaxInput)) {
            sql+="and "+ParkingsViewContract.COLUMN_NAME_STANDARD_LOTS+"+"+ParkingsViewContract.COLUMN_NAME_DISABLED_LOTS+"<="+lotsMaxInput+" ";
        }
        if(isRoofed)
        {
            sql+="and "+ParkingsViewContract.COLUMN_NAME_IS_ROOF+"=1 ";
        }
        if(isGuarded)
        {
            sql+="and "+ParkingsViewContract.COLUMN_NAME_IS_GUARDED+"=1 ";
        }
        if(hasFreeLots)
        {
            sql+="and "+ParkingsViewContract.COLUMN_NAME_STANDARD_LOTS+"+"+ParkingsViewContract.COLUMN_NAME_DISABLED_LOTS+">"+ParkingsViewContract.COLUMN_NAME_OCCUPIED_LOTS;
        }
        return sql;
    }
    private static ParkingsView generateParkingsView(CachedRowSet resultSet) throws SQLException {
        ParkingsView parkingsView = new ParkingsView();


        parkingsView.setId(resultSet.getInt(ParkingsViewContract.COLUMN_NAME_ID));
        parkingsView.setStandardLots(resultSet.getInt(ParkingsViewContract.COLUMN_NAME_STANDARD_LOTS));
        parkingsView.setDisabledLots(resultSet.getInt(ParkingsViewContract.COLUMN_NAME_DISABLED_LOTS));
        parkingsView.setOccupiedLots(resultSet.getInt(ParkingsViewContract.COLUMN_NAME_OCCUPIED_LOTS));
        parkingsView.setRoofed(resultSet.getBoolean(ParkingsViewContract.COLUMN_NAME_IS_ROOF));
        parkingsView.setGuarded(resultSet.getBoolean(ParkingsViewContract.COLUMN_NAME_IS_GUARDED));
        parkingsView.setLastControl(resultSet.getDate(ParkingsViewContract.COLUMN_NAME_LAST_CONTROL));
        parkingsView.setMaxWeight(resultSet.getFloat(ParkingsViewContract.COLUMN_NAME_MAX_WEIGHT));
        parkingsView.setMaxHeight(resultSet.getFloat(ParkingsViewContract.COLUMN_NAME_MAX_HEIGHT));
        parkingsView.setLocationID(resultSet.getInt(ParkingsViewContract.COLUMN_NAME_LOCATION_ID));
        parkingsView.setParkType(resultSet.getString(ParkingsViewContract.COLUMN_NAME_PARK_TYPE));
        parkingsView.setZonePaid(resultSet.getBoolean(ParkingsViewContract.COLUMN_NAME_IS_ZONE_PAID));
        parkingsView.setEstateName(resultSet.getString(ParkingsViewContract.COLUMN_NAME_ESTATE_NAME));
        parkingsView.setGated(resultSet.getBoolean(ParkingsViewContract.COLUMN_NAME_ARE_GATES));
        parkingsView.setMaxStopMinutes(resultSet.getInt(ParkingsViewContract.COLUMN_NAME_MAX_STOP_MINUTES));
        parkingsView.setCommunicationNode(resultSet.getString(ParkingsViewContract.COLUMN_NAME_COMMUNICATION_NODE_));
        parkingsView.setAutomatic(resultSet.getBoolean(ParkingsViewContract.COLUMN_NAME_IS_AUTOMATIC));

        return parkingsView;
    }

    private static void generateSlotsStats(List<ParkingsView> parkingsViews)
    {
        nrStandardSlots = 0;
        nrDisabledSlots = 0;
        nrOccupiedSlots = 0;
        for(ParkingsView p : parkingsViews)
        {
            nrStandardSlots+=p.getStandardLots();
            nrDisabledSlots+=p.getDisabledLots();
            nrOccupiedSlots+=p.getOccupiedLots();
        }


    }
    private static void generateParkTimeStats(String heightMinInput, String heightMaxInput, String weightMinInput,
                                              String weightMaxInput, String lotsMinInput, String lotsMaxInput, List<String> parkingType,
                                              boolean isRoofed, boolean isGuarded, boolean hasFreeLots) {
        String sql;
        if(parkingType.size()!=0)
        {
            if(parkingType.size()==1)
            {
                sql="SELECT "+ParkingsViewContract.TABLE_NAME+".*, [dbo].[parks].date_time FROM "+ParkingsViewContract.TABLE_NAME+
                        "INNER JOIN "+parkingType.get(0)+" ON "+ParkingsViewContract.TABLE_NAME+"."+
                        ParkingsViewContract.COLUMN_NAME_ID+"="+parkingType+"."+ParkingsViewContract.COLUMN_NAME_ID;
            }
            else {
                sql = "SELECT " + ParkingsViewContract.TABLE_NAME + ".*, [dbo].[parks].date_time FROM " + parkingType.get(0);
                for (int i = 1; i < parkingType.size(); i++) {
                    sql += " full join "+parkingType.get(i) + " on " + parkingType.get(0) + "." + ParkingsViewContract.COLUMN_NAME_ID +
                            "=" + parkingType.get(i) + "." + ParkingsViewContract.COLUMN_NAME_ID;
                }
                sql+=" inner join "+ParkingsViewContract.TABLE_NAME+" on ";
                for(String s : parkingType)
                {
                    sql+=s+"."+ParkingsViewContract.COLUMN_NAME_ID+"="+ParkingsViewContract.TABLE_NAME+"."+ParkingsViewContract.COLUMN_NAME_ID+" or ";
                }
                sql+="1=0";
            }
        }
        else
        {
            sql = "SELECT "+ParkingsViewContract.TABLE_NAME+".*, [dbo].[parks].date_time FROM " + ParkingsViewDAO.ParkingsViewContract.TABLE_NAME+" ";
        }

        sql += " right JOIN [dbo].[parks] on "+ ParkingsViewContract.TABLE_NAME+"."+ParkingsViewContract.COLUMN_NAME_ID+ "= [dbo].[parks].parking_id";

        sql+=generateWherePartOfQuery(heightMinInput, heightMaxInput, weightMinInput,
                                        weightMaxInput, lotsMinInput, lotsMaxInput,
                                        isRoofed, isGuarded, hasFreeLots);
        CachedRowSet resultSet = DbHelper.executeQuery(sql);
        List<Long> dateDifferences = new ArrayList<>();
        try {
            while (resultSet!=null&&resultSet.next())
            {
                java.sql.Date sqlDate = resultSet.getDate("date_time");
                DateTime dateTime = new DateTime(sqlDate.getTime());
                Interval interval = new Interval(dateTime, new org.joda.time.Instant());
                Duration duration = new Duration(interval);
                dateDifferences.add(duration.getStandardHours());
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        avParkTime = (float)dateDifferences.stream().mapToDouble(a->a).average().orElse(0.0);
        longestParkTime = (float)dateDifferences.stream().mapToDouble(a->a).max().orElse(0.0);
        shortestParkTime = (float)dateDifferences.stream().mapToDouble(a->a).min().orElse(0.0);
    }

    public static void generateStatistics(List<ParkingsView> parkingsViewList, String heightMinInput, String heightMaxInput, String weightMinInput,
                                          String weightMaxInput, String lotsMinInput, String lotsMaxInput, List<String> parkingType,
                                          boolean isRoofed, boolean isGuarded, boolean hasFreeLots)
    {
        nrOfParkings = parkingsViewList.size();
        nrCityParkings = nrParkRides = nrKissRides = nrEstateParkings = 0;
        if(parkingType.size()==0||parkingType.stream().anyMatch(e->e.equals(PARKING_TYPES_TABLE_NAMES.get(0)))) {
            nrCityParkings = getParkingsViews(heightMinInput, heightMaxInput,
                        weightMinInput, weightMaxInput,
                        lotsMinInput, lotsMaxInput,
                        new ArrayList<>(Arrays.asList(PARKING_TYPES_TABLE_NAMES.get(0))), isRoofed,
                        isGuarded, hasFreeLots).size();
        }
        if(parkingType.size()==0||parkingType.stream().anyMatch(e->e.equals(PARKING_TYPES_TABLE_NAMES.get(1)))) {
            nrParkRides = getParkingsViews(heightMinInput, heightMaxInput,
                    weightMinInput, weightMaxInput,
                    lotsMinInput, lotsMaxInput,
                    new ArrayList<>(Arrays.asList(PARKING_TYPES_TABLE_NAMES.get(1))), isRoofed,
                    isGuarded, hasFreeLots).size();
        }
        if(parkingType.size()==0||parkingType.stream().anyMatch(e->e.equals(PARKING_TYPES_TABLE_NAMES.get(2)))) {
            nrKissRides = getParkingsViews(heightMinInput, heightMaxInput,
                    weightMinInput, weightMaxInput,
                    lotsMinInput, lotsMaxInput,
                    new ArrayList<>(Arrays.asList(PARKING_TYPES_TABLE_NAMES.get(2))), isRoofed,
                    isGuarded, hasFreeLots).size();
        }
        if(parkingType.size()==0||parkingType.stream().anyMatch(e->e.equals(PARKING_TYPES_TABLE_NAMES.get(3)))) {
            nrEstateParkings = getParkingsViews(heightMinInput, heightMaxInput,
                    weightMinInput, weightMaxInput,
                    lotsMinInput, lotsMaxInput,
                    new ArrayList<>(Arrays.asList(PARKING_TYPES_TABLE_NAMES.get(3))), isRoofed,
                    isGuarded, hasFreeLots).size();
        }
        generateSlotsStats(parkingsViewList);
        if(nrStandardSlots+nrDisabledSlots==0)
            avOccupancy=0;
        else
            avOccupancy = (float) nrOccupiedSlots / (float) (nrStandardSlots + nrDisabledSlots) * 100;

        generateParkTimeStats(heightMinInput, heightMaxInput,
                weightMinInput, weightMaxInput,
                lotsMinInput, lotsMaxInput,
                parkingType, isRoofed,
                isGuarded, hasFreeLots);

    }




    public static int getNrOfParkings() {
        return nrOfParkings;
    }

    public static int getNrCityParkings() {
        return nrCityParkings;
    }

    public static int getNrParkRides() {
        return nrParkRides;
    }

    public static int getNrKissRides() {
        return nrKissRides;
    }

    public static int getNrEstateParkings() {
        return nrEstateParkings;
    }

    public static int getNrStandardSlots() {
        return nrStandardSlots;
    }

    public static int getNrDisabledSlots() {
        return nrDisabledSlots;
    }

    public static int getNrOccupiedSlots() {
        return nrOccupiedSlots;
    }

    public static float getAvParkTime() {
        return avParkTime;
    }

    public static float getAvOccupancy() {
        return avOccupancy;
    }

    public static float getLongestParkTime() {
        return longestParkTime;
    }

    public static float getShortestParkTime() {
        return shortestParkTime;
    }
}
