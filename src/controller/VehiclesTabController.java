package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.country.Country;
import model.engine.Engine;

public class VehiclesTabController {

    @FXML
    public TextField vheightMinInput;
    @FXML
    public TextField weightMinInput;
    @FXML
    public TextField vheightMaxInput;
    @FXML
    public TextField weightMaxInput;
    @FXML
    public ChoiceBox<Engine> engineTypeSelect;
    @FXML
    public ChoiceBox<Country> vehiclesCountrySelect;
    @FXML
    public CheckBox parkedCheckBox;
    @FXML
    public ChoiceBox<String> vehiclesColumnsSelect;

    @FXML
    public void onVehiclesFilterClicked() {
    }

}
