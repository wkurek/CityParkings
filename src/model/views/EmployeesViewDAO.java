package model.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.country.Country;
import model.country.CountryDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeesViewDAO {


    private static final ArrayList<String> COLUMN_NAMES = new ArrayList<>(Arrays.asList(
            "ID",
            "Name",
            "Surname",
            "Salary",
            "Country",
            "City",
            "ZIP Code",
            "Street",
            "Number",
            "Department Name",
            "Parking ID",
            "Parking's last control"));

    public static List<String> getColumnsNames()
    {
        return COLUMN_NAMES;
    }
}
