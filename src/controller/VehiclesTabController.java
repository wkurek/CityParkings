package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuButton;
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
    public CheckBox parkedCheckBox;
    @FXML
    public MenuButton engineTypeMenuButton;
    @FXML
    public MenuButton countryMenuButton;
    @FXML
    public MenuButton columnsMenuButton;

    public VehiclesTabController()
    {
        
    }
    @FXML
    private void initialize()
    {

    }
    @FXML
    public void onVehiclesFilterClicked() {
    }

}
