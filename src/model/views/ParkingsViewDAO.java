package model.views;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParkingsViewDAO {
    private static final List<String> COLUMN_NAMES = new ArrayList<>(Arrays.asList(
            "ID",
            "Standard Lots",
            "Disabled Lots",
            "Occupied Lots",
            "Roofed",
            "Guarded",
            "Last control",
            "Max weight",
            "Max height",
            "Location ID",
            "Park type",
            "In paid zone",
            "Estate name",
            "Has gates",
            "Max stop time(min)",
            "Communication node",
            "Is automatic"
    ));
    private static final List<String> PARKING_TYPES = new ArrayList<>(Arrays.asList(
            "City Parkings",
            "Kiss & Ride Parkings",
            "Park & Ride Parkings",
            "Estate Parkings",
            "Undefined"
    ));

    public static List<String> getColumnsNames()
    {
        return COLUMN_NAMES;
    }

    public static List<String> getParkingTypes() {
        return PARKING_TYPES;
    }
}
