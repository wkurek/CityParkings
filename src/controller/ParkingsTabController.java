package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.parking.Parking;
import model.views.ParkingsViewDAO;

import java.util.ArrayList;
import java.util.List;

public class ParkingsTabController {

    @FXML
    private CheckBox roofedCheckBox;
    @FXML
    private CheckBox guardedCheckBox;
    @FXML
    private CheckBox freeLotsCheckBox;
    @FXML
    private TextField lotsMaxInput;
    @FXML
    private TextField lotsMinInput;
    @FXML
    private TextField pheightMinInput;
    @FXML
    private TextField pheightMaxInput;
    @FXML
    private TextField maxWeightInput;
    @FXML
    private MenuButton columnMenuButton;
    @FXML
    private MenuButton parkingTypeMenuButton;

    private List<CheckMenuItem> columnItems;
    private List<CheckMenuItem> parkingTypeItems;

    public ParkingsTabController()
    {
        columnItems = new ArrayList<>();
        parkingTypeItems = new ArrayList<>();
    }
    @FXML
    private void initialize()
    {
        parkingTypeItems.add(new CheckMenuItem("Select All"));
        for(String i : ParkingsViewDAO.getParkingTypes()) {
            parkingTypeItems.add(new CheckMenuItem(i));
        }
        parkingTypeMenuButton.getItems().setAll(parkingTypeItems);


        columnItems.add(new CheckMenuItem("Select All"));
        for(String i : ParkingsViewDAO.getColumnsNames()) {
            columnItems.add(new CheckMenuItem(i));
        }
        columnMenuButton.getItems().setAll(columnItems);
    }
    @FXML
    public void onParkingsFilterClicked() {
    }
}
