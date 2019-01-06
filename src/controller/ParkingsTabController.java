package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.views.ParkingsView;
import model.views.ParkingsViewDAO;


import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;

public class ParkingsTabController {
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
            "Communication node",
            "Is automatic",
            "Has gates",
            "Max stop time(min)",
            "Estate name"
    ));
    private static final List<String> PARKING_TYPES = new ArrayList<>(Arrays.asList(
            "City Parkings",
            "Park & Ride Parkings",
            "Kiss & Ride Parkings",
            "Estate Parkings"
    ));
    private static final List<String> PARKING_TYPES_TABLE_NAMES = new ArrayList<>(Arrays.asList(
            "city_parkings",
            "park_rides",
            "kiss_rides",
            "estate_parkings"
    ));

    @FXML
    public TableView parkingsViewTable;
    private Stage stage;
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
    //private Task<ObservableList<ParkingsView>> parkingsViewLoadTask;

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
    }

    private void menuButtonsSet() {
        parkingTypeItems.add(new CheckMenuItem("Select All"));
        for(String i : PARKING_TYPES) {
            parkingTypeItems.add(new CheckMenuItem(i));
        }
        parkingTypeMenuButton.getItems().setAll(parkingTypeItems);
        columnItems.add(new CheckMenuItem("Select All"));
        for(String i : COLUMN_NAMES) {
            columnItems.add(new CheckMenuItem(i));
        }
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
        setBooleanColumns(roofed);
        roofed.setCellValueFactory(param->param.getValue().roofedProperty().asString());
        columns.add(roofed);
        TableColumn<ParkingsView, String> guarded = new TableColumn<>(COLUMN_NAMES.get(5));
        setBooleanColumns(guarded);
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
        setBooleanColumns(inPaidZone);
        inPaidZone.setCellValueFactory(param->param.getValue().zonePaidProperty().asString());
        columns.add(inPaidZone);
        TableColumn<ParkingsView, String> comNode = new TableColumn<>(COLUMN_NAMES.get(12));
        comNode.setCellValueFactory(param->param.getValue().communicationNodeProperty());
        columns.add(comNode);
        TableColumn<ParkingsView, String> isAutomatic = new TableColumn<>(COLUMN_NAMES.get(13));
        setBooleanColumns(isAutomatic);
        isAutomatic.setCellValueFactory(param->param.getValue().automaticProperty().asString());
        columns.add(isAutomatic);
        TableColumn<ParkingsView, String> hasGates = new TableColumn<>(COLUMN_NAMES.get(14));
        setBooleanColumns(hasGates);
        hasGates.setCellValueFactory(param->param.getValue().gatedProperty().asString());
        columns.add(hasGates);
        TableColumn<ParkingsView, Integer> maxStop = new TableColumn<>(COLUMN_NAMES.get(15));
        setIntegerColumnsNullable(maxStop);
        maxStop.setCellValueFactory(param->param.getValue().maxStopMinutesProperty().asObject());
        columns.add(maxStop);
        TableColumn<ParkingsView, String> estateName = new TableColumn<>(COLUMN_NAMES.get(16));
        estateName.setCellValueFactory(param->param.getValue().estateNameProperty());
        columns.add(estateName);
    }


    @FXML
    public void onParkingsFilterClicked() {
        setUpTable();
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
                parkTypes.add(PARKING_TYPES_TABLE_NAMES.get(i-1));
        }
        return parkTypes;
    }
    private void setBooleanColumns(TableColumn<ParkingsView, String> columnName)
    {
        columnName.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    if (item.equals("true")) {
                        setText("Yes");
                    } else {
                        setText("No");
                    }
                }
            }
        });
    }
    private void setIntegerColumnsNullable(TableColumn<ParkingsView, Integer> columnName)
    {
        columnName.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    int value = item;
                    if (value == 0) {
                        setText("");
                    } else {
                        setText(Integer.toString(value));
                    }
                }
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
