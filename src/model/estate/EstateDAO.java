package model.estate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class EstateDAO {
    public static class EstateContract {
        public static final String TABLE_NAME = "[dbo].[estates]";
        public static final String COLUMN_NAME_ESTATE_NAME = "estate_name";
    }

    public static Estate generateEstate(CachedRowSet resultSet) throws SQLException {
        Estate estate = new Estate();
        estate.setEstateName(resultSet.getString(EstateContract.COLUMN_NAME_ESTATE_NAME));

        return estate;
    }

    private static ObservableList<Estate> generateEstateList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Estate> estateList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Estate estate = generateEstate(resultSet);
            estateList.add(estate);
        }

        return estateList;
    }

    public static ObservableList<Estate> getEstate() {
        String sql = String.format("SELECT * FROM %s", EstateContract.TABLE_NAME);
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Estate> estateList = null;

        try {
            estateList = generateEstateList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return estateList;
    }

    public static Estate getEstate(String estateName) {
        String sql = String.format("SELECT * FROM %s WHERE %s=%s", EstateContract.TABLE_NAME,
                EstateContract.COLUMN_NAME_ESTATE_NAME, estateName);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Estate estate = null;

        try {
            estate = generateEstate(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return estate;
    }

    public static void saveEstate(Estate estate) {
        if(estate == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES ('%s')", EstateContract.TABLE_NAME, estate.getEstateName());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteEstate(String estateName) {
        String sql = String.format("DELETE FROM %s WHERE %s = %s", EstateContract.TABLE_NAME,
                EstateContract.COLUMN_NAME_ESTATE_NAME, estateName);

        DbHelper.executeUpdateQuery(sql);
    }
}
