package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import model.views.ParkingsView;
import model.views.ParkingsViewDAO;


import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;

public class ParkingsTabController {
    private static final List<String> COLUMN_NAMES = new ArrayList<>(Arrays.asList(
            "ID",
            "Standard Slots",
            "Disabled Slots",
            "Occupied Slots",
            "Roofed",
            "Guarded",
            "Last control",
            "Max weight",
            "Max height",
            "Location ID",
            "Park type",
            "In paid zone",
            "Communication node",
            "Is automatic",
            "Has gates",
            "Max stop time(min)",
            "Estate name"
    ));
    static final List<String> PARKING_TYPES = new ArrayList<>(Arrays.asList(
            "City Parkings",
            "Park & Ride Parkings",
            "Kiss & Ride Parkings",
            "Estate Parkings"
    ));


    @FXML
    public TableView parkingsViewTable;
    @FXML
    public Text nrOfParkings;
    @FXML
    public Text nrCityParkings;
    @FXML
    public Text nrParkRides;
    @FXML
    public Text nrKissRides;
    @FXML
    public Text nrEstateParkings;
    @FXML
    public Text nrStandardSlots;
    @FXML
    public Text nrDisabledSlots;
    @FXML
    public Text nrOccupiedSlots;
    @FXML
    public Text avParkTime;
    @FXML
    public Text avOccupancy;
    @FXML
    public Text longestParkTime;
    @FXML
    public Text shortestParkTime;
    @FXML
    public TextField heightMinInput;
    @FXML
    public TextField heightMaxInput;
    @FXML
    public TextField weightMaxInput;
    @FXML
    public TextField weightMinInput;
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
    private MenuButton columnMenuButton;
    @FXML
    private MenuButton parkingTypeMenuButton;

    private List<CheckMenuItem> columnItems;
    private List<CheckMenuItem> parkingTypeItems;

    private ObservableList<ParkingsView> parkingsViewsList;

    private List<TableColumn> columns;

    public ParkingsTabController()
    {
        columns = new ArrayList<>();
        columnItems = new ArrayList<>();
        parkingTypeItems = new ArrayList<>();
        parkingsViewsList = FXCollections.observableArrayList();
    }
    @FXML
    private void initialize()
    {
        menuButtonsSet();
        generateTableColumns();
        setUpTable();
        setUpStatistics();
    }

    private void menuButtonsSet() {
        parkingTypeItems.add(new CheckMenuItem("Select All"));
        PARKING_TYPES.forEach(e->parkingTypeItems.add(new CheckMenuItem(e)));
        parkingTypeMenuButton.getItems().setAll(parkingTypeItems);

        columnItems.add(new CheckMenuItem("Select All"));
        COLUMN_NAMES.forEach(e->columnItems.add(new CheckMenuItem(e)));
        columnMenuButton.getItems().setAll(columnItems);
    }
    private void generateTableColumns()
    {
        columns = new ArrayList<>();
        TableColumn<ParkingsView, Integer> ID = new TableColumn<>(COLUMN_NAMES.get(0));
        ID.setCellValueFactory(param->param.getValue().idProperty().asObject());
        columns.add(ID);
        TableColumn<ParkingsView, Integer> standardLots = new TableColumn<>(COLUMN_NAMES.get(1));
        standardLots.setCellValueFactory(param->param.getValue().standardLotsProperty().asObject());
        columns.add(standardLots);
        TableColumn<ParkingsView, Integer> disabledLots = new TableColumn<>(COLUMN_NAMES.get(2));
        disabledLots.setCellValueFactory(param->param.getValue().disabledLotsProperty().asObject());
        columns.add(disabledLots);
        TableColumn<ParkingsView, Integer> occupiedLots = new TableColumn<>(COLUMN_NAMES.get(3));
        occupiedLots.setCellValueFactory(param->param.getValue().occupiedLotsProperty().asObject());
        columns.add(occupiedLots);
        TableColumn<ParkingsView, String> roofed = new TableColumn<>(COLUMN_NAMES.get(4));
        ReportsController.setBooleanColumns(roofed);
        roofed.setCellValueFactory(param->param.getValue().roofedProperty().asString());
        columns.add(roofed);
        TableColumn<ParkingsView, String> guarded = new TableColumn<>(COLUMN_NAMES.get(5));
        ReportsController.setBooleanColumns(guarded);
        guarded.setCellValueFactory(param->param.getValue().guardedProperty().asString());
        columns.add(guarded);
        TableColumn<ParkingsView, Date> lastControl = new TableColumn<>(COLUMN_NAMES.get(6));
        lastControl.setCellValueFactory(param -> param.getValue().lastControlProperty());
        columns.add(lastControl);
        TableColumn<ParkingsView, Float> maxWeight = new TableColumn<>(COLUMN_NAMES.get(7));
        maxWeight.setCellValueFactory(param->param.getValue().maxWeightProperty().asObject());
        columns.add(maxWeight);
        TableColumn<ParkingsView, Float> maxHeight = new TableColumn<>(COLUMN_NAMES.get(8));
        maxHeight.setCellValueFactory(param->param.getValue().maxHeightProperty().asObject());
        columns.add(maxHeight);
        TableColumn<ParkingsView, Integer> locationID = new TableColumn<>(COLUMN_NAMES.get(9));
        locationID.setCellValueFactory(param->param.getValue().locationIDProperty().asObject());
        columns.add(locationID);
        TableColumn<ParkingsView, String> parkType = new TableColumn<>(COLUMN_NAMES.get(10));
        parkType.setCellValueFactory(param->param.getValue().parkTypeProperty());
        columns.add(parkType);
        TableColumn<ParkingsView, String> inPaidZone = new TableColumn<>(COLUMN_NAMES.get(11));
        ReportsController.setBooleanColumns(inPaidZone);
        inPaidZone.setCellValueFactory(param->param.getValue().zonePaidProperty().asString());
        columns.add(inPaidZone);
        TableColumn<ParkingsView, String> comNode = new TableColumn<>(COLUMN_NAMES.get(12));
        comNode.setCellValueFactory(param->param.getValue().communicationNodeProperty());
        columns.add(comNode);
        TableColumn<ParkingsView, String> isAutomatic = new TableColumn<>(COLUMN_NAMES.get(13));
        ReportsController.setBooleanColumns(isAutomatic);
        isAutomatic.setCellValueFactory(param->param.getValue().automaticProperty().asString());
        columns.add(isAutomatic);
        TableColumn<ParkingsView, String> hasGates = new TableColumn<>(COLUMN_NAMES.get(14));
        ReportsController.setBooleanColumns(hasGates);
        hasGates.setCellValueFactory(param->param.getValue().gatedProperty().asString());
        columns.add(hasGates);
        TableColumn<ParkingsView, Integer> maxStop = new TableColumn<>(COLUMN_NAMES.get(15));
        ReportsController.setIntegerColumnsNullable(maxStop);
        maxStop.setCellValueFactory(param->param.getValue().maxStopMinutesProperty().asObject());
        columns.add(maxStop);
        TableColumn<ParkingsView, String> estateName = new TableColumn<>(COLUMN_NAMES.get(16));
        estateName.setCellValueFactory(param->param.getValue().estateNameProperty());
        columns.add(estateName);
    }


    @FXML
    public void onParkingsFilterClicked() {
        setUpTable();
        setUpStatistics();
    }

    private void setUpTable()
    {
        ReportsController.setColumns(parkingsViewTable, columns, columnMenuButton);
        parkingsViewsList = ParkingsViewDAO.getParkingsViews(heightMinInput.getText(), heightMaxInput.getText(), weightMinInput.getText(), weightMaxInput.getText(),
                lotsMinInput.getText(), lotsMaxInput.getText(),
                parkTypesMenuButtonToParkTypesColumnNames(),
                roofedCheckBox.isSelected(), guardedCheckBox.isSelected(), freeLotsCheckBox.isSelected());
        parkingsViewTable.setItems(parkingsViewsList);
    }

    private List<String> parkTypesMenuButtonToParkTypesColumnNames()
    {
        List<String> parkTypes = new ArrayList<>();
        if(((CheckMenuItem)parkingTypeMenuButton.getItems().get(0)).isSelected())
            return parkTypes;
        for(int i = 1; i<parkingTypeMenuButton.getItems().size();i++)
        {
            if(((CheckMenuItem)parkingTypeMenuButton.getItems().get(i)).isSelected())
                parkTypes.add(ParkingsViewDAO.PARKING_TYPES_TABLE_NAMES.get(i-1));
        }
        return parkTypes;
    }

    private void setUpStatistics()
    {
        ParkingsViewDAO.generateStatistics(heightMinInput.getText(), heightMaxInput.getText(), weightMinInput.getText(), weightMaxInput.getText(),
                lotsMinInput.getText(), lotsMaxInput.getText(),
                parkTypesMenuButtonToParkTypesColumnNames(),
                roofedCheckBox.isSelected(), guardedCheckBox.isSelected(), freeLotsCheckBox.isSelected());
        nrOfParkings.setText(Integer.toString(ParkingsViewDAO.getNrOfParkings()));
        nrCityParkings.setText(Integer.toString(ParkingsViewDAO.getNrCityParkings()));
        nrParkRides.setText(Integer.toString(ParkingsViewDAO.getNrParkRides()));
        nrKissRides.setText(Integer.toString(ParkingsViewDAO.getNrKissRides()));
        nrEstateParkings.setText(Integer.toString(ParkingsViewDAO.getNrEstateParkings()));
        nrStandardSlots.setText(Integer.toString(ParkingsViewDAO.getNrStandardSlots()));
        nrDisabledSlots.setText(Integer.toString(ParkingsViewDAO.getNrDisabledSlots()));
        nrOccupiedSlots.setText(Integer.toString(ParkingsViewDAO.getNrOccupiedSlots()));
        avParkTime.setText(String.format("%.02f", ParkingsViewDAO.getAvParkTime()));
        avOccupancy.setText(String.format("%.02f",ParkingsViewDAO.getAvOccupancy()));
        longestParkTime.setText(String.format("%.02f",ParkingsViewDAO.getLongestParkTime()));
        shortestParkTime.setText(String.format("%.02f",ParkingsViewDAO.getShortestParkTime()));
    }

}
