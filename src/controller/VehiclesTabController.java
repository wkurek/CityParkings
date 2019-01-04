package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.country.Country;
import model.country.CountryDAO;
import model.engine.Engine;
import model.engine.EngineDAO;
import model.views.VehiclesViewDAO;

import java.util.ArrayList;
import java.util.List;

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
    public MenuButton columnMenuButton;

    private List<CheckMenuItem> engineTypeItems;
    private List<CheckMenuItem> countryItems;
    private List<CheckMenuItem> columnItems;

    public VehiclesTabController()
    {
        countryItems = new ArrayList<>();
        engineTypeItems = new ArrayList<>();
        columnItems = new ArrayList<>();
    }
    @FXML
    private void initialize()
    {
        countryItems.add(new CheckMenuItem("Select All"));
        for(Country i : CountryDAO.getCountries()) {
            countryItems.add(new CheckMenuItem(i.toString()));
        }
        countryMenuButton.getItems().setAll(countryItems);

        engineTypeItems.add(new CheckMenuItem("Select All"));
        for(Engine i : EngineDAO.getEngines()) {
            engineTypeItems.add(new CheckMenuItem(i.toString()));
        }
        engineTypeMenuButton.getItems().setAll(engineTypeItems);

        columnItems.add(new CheckMenuItem("Select All"));
        for(String i : VehiclesViewDAO.getColumnsNames()) {
            columnItems.add(new CheckMenuItem(i));
        }
        columnMenuButton.getItems().setAll(columnItems);
    }
    @FXML
    public void onVehiclesFilterClicked() {
    }

}
