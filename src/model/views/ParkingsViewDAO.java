package model.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;
import util.Validator;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.List;

public class ParkingsViewDAO {

    public static class ParkingsViewContract {
        public static final String TABLE_NAME = "[dbo].[parkings_view]";
        public static final String COLUMN_NAME_ID = "parking_id";
        public static final String COLUMN_NAME_STANDARD_LOTS = "standard_lots";
        public static final String COLUMN_NAME_DISABLED_LOTS = "disabled_lots";
        public static final String COLUMN_NAME_OCCUPIED_LOTS = "occupied_lots";
        public static final String COLUMN_NAME_IS_ROOF = "is_roof";
        public static final String COLUMN_NAME_IS_GUARDED = "is_guarded";
        public static final String COLUMN_NAME_LAST_CONTROL = "last_control";
        public static final String COLUMN_NAME_MAX_WEIGHT = "max_weight";
        public static final String COLUMN_NAME_MAX_HEIGHT = "max_height";
        public static final String COLUMN_NAME_LOCATION_ID = "location_ID";
        public static final String COLUMN_NAME_PARK_TYPE = "park_type";
        public static final String COLUMN_NAME_IS_ZONE_PAID = "is_zone_paid";
        public static final String COLUMN_NAME_ESTATE_NAME = "estate_name";
        public static final String COLUMN_NAME_ARE_GATES = "are_gates";
        public static final String COLUMN_NAME_MAX_STOP_MINUTES = "max_stop_minutes";
        public static final String COLUMN_NAME_COMMUNICATION_NODE_ = "communication_node";
        public static final String COLUMN_NAME_IS_AUTOMATIC = "is_automatic";
    }

    public static ObservableList<ParkingsView> getParkingsViews(String heightMinInput, String heightMaxInput, String weightMinInput,
                                                                String weightMaxInput, String lotsMinInput, String lotsMaxInput, List<String> parkingType,
                                                                boolean isRoofed, boolean isGuarded, boolean hasFreeLots)
    {
        String sql = generateSelectWhereQuery(heightMinInput, heightMaxInput,
                                              weightMinInput, weightMaxInput,
                                              lotsMinInput, lotsMaxInput,
                                              parkingType, isRoofed,
                                              isGuarded, hasFreeLots);

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<ParkingsView> parkingsViewList = null;
        try {
             parkingsViewList = generateParkingsViewsList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return parkingsViewList;
    }

    private static String generateSelectWhereQuery(String heightMinInput, String heightMaxInput, String weightMinInput,
                                                   String weightMaxInput, String lotsMinInput, String lotsMaxInput, List<String> parkingType,
                                                   boolean isRoofed, boolean isGuarded, boolean hasFreeLots)
    {
        String sql = generateSelectQuery();
        sql+=" WHERE 1=1 ";
        if(heightMinInput!=null && Validator.isHeightValid(heightMinInput)){
            sql+="and "+ParkingsViewContract.COLUMN_NAME_MAX_HEIGHT+">="+heightMinInput+" ";
        }
        if(heightMaxInput!=null && Validator.isHeightValid(heightMaxInput)){
            sql+="and "+ParkingsViewContract.COLUMN_NAME_MAX_HEIGHT+"<="+heightMaxInput+" ";
        }
        if(weightMinInput!=null && Validator.isWeightValid(weightMinInput)){
            sql+="and "+ParkingsViewContract.COLUMN_NAME_MAX_WEIGHT+">="+weightMinInput+" ";
        }
        if(weightMaxInput!=null && Validator.isWeightValid(weightMaxInput)){
            sql+="and "+ParkingsViewContract.COLUMN_NAME_MAX_WEIGHT+"<="+weightMaxInput+" ";
        }
        if(lotsMinInput!=null && Validator.isIntegerInputValid(lotsMinInput)){
            sql+="and "+ParkingsViewContract.COLUMN_NAME_STANDARD_LOTS+"+"+ParkingsViewContract.COLUMN_NAME_DISABLED_LOTS+">="+lotsMinInput+" ";
        }
        if(lotsMaxInput!=null && Validator.isIntegerInputValid(lotsMaxInput)){
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

    private static ObservableList<ParkingsView> generateParkingsViewsList(CachedRowSet resultSet) throws SQLException{
        ObservableList<ParkingsView> parkingsViewsList = FXCollections.observableArrayList();

        while (resultSet!=null&&resultSet.next()) {
            ParkingsView parkingsView = generateParkingsView(resultSet);
            parkingsViewsList.add(parkingsView);
        }

        return parkingsViewsList;
    }

    private static String generateSelectQuery()
    {
        return "SELECT * FROM " + ParkingsViewDAO.ParkingsViewContract.TABLE_NAME;
    }
    public static ParkingsView generateParkingsView(CachedRowSet resultSet) throws SQLException {
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

}
