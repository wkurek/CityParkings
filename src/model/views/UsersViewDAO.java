package model.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersViewDAO {

    private static final ArrayList<String> COLUMN_NAMES = new ArrayList<>(Arrays.asList(
            "ID",
            "Name",
            "Surname",
            "Phone number",
            "Country",
            "City",
            "ZIP Code",
            "Street",
            "Number",
            "Number of vehicles",
            "Card ID",
            "Card expiration date"));
    public static List<String> getColumnsNames()
    {
        return COLUMN_NAMES;
    }

}
