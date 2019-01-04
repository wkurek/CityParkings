package model.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VehiclesViewDAO {

    private static final ArrayList<String> COLUMN_NAMES = new ArrayList<>(Arrays.asList(
            "ID",
            "Plate number",
            "Height",
            "Weight",
            "Engine Type",
            "Owner name",
            "Owner surname",
            "Owner phone nr",
            "Owner country",
            "Owner city",
            "Owner city ZIP code",
            "Owner street",
            "Owner home nr",
            "Parked on (parking id)",
            "Parked at (time)"));


    public static List<String> getColumnsNames() {
        return COLUMN_NAMES;
    }
}
