package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.parking.Parking;
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
    private Task<ObservableList<ParkingsView>> parkingsViewLoadTask;

    private List<TableColumn> columns;

    public ParkingsTabController()
    {
        columns = new ArrayList<>();
        columnItems = new ArrayList<>();
        parkingTypeItems = new ArrayList<>();
        parkingsViewsList = FXCollections.observableArrayList();

        parkingsViewLoadTask = generateParkingsViewsLoadTask();
    }
    @FXML
    private void initialize()
    {
        menuButtonsSet();
        generateTableColumns();
        ReportsController.setColumns(parkingsViewTable, columns, columnMenuButton);
        parkingsViewTable.setItems(parkingsViewsList);
        scheduleLoadTask(parkingsViewLoadTask);
    }
    private void menuButtonsSet() {
        columnItems.add(new CheckMenuItem("Select All"));
        for(String i : COLUMN_NAMES) {
            columnItems.add(new CheckMenuItem(i));
        }
        columnMenuButton.getItems().setAll(columnItems);

        parkingTypeItems.add(new CheckMenuItem("Select All"));
        for(String i : PARKING_TYPES) {
            parkingTypeItems.add(new CheckMenuItem(i));
        }
        parkingTypeMenuButton.getItems().setAll(parkingTypeItems);
    }
    private void generateTableColumns()
    {
        columns = new ArrayList<>();
        TableColumn<ParkingsView, Integer> ID = new TableColumn<>("ID");
        ID.setCellValueFactory(param->param.getValue().idProperty().asObject());
        columns.add(ID);
        TableColumn<ParkingsView, Integer> stadardLots = new TableColumn<>("Standard Lots");
        stadardLots.setCellValueFactory(param->param.getValue().standardLotsProperty().asObject());
        columns.add(stadardLots);
        TableColumn<ParkingsView, Integer> disabledLots = new TableColumn<>("Lots for disabled");
        disabledLots.setCellValueFactory(param->param.getValue().disabledLotsProperty().asObject());
        columns.add(disabledLots);
        TableColumn<ParkingsView, Integer> occupiedLots = new TableColumn<>("Occupied Lots");
        occupiedLots.setCellValueFactory(param->param.getValue().occupiedLotsProperty().asObject());
        columns.add(occupiedLots);
        TableColumn<ParkingsView, String> roofed = new TableColumn<>("Is roofed");
        roofed.setCellValueFactory(param->param.getValue().roofedProperty().asString());
        columns.add(roofed);
        TableColumn<ParkingsView, String> guarded = new TableColumn<>("Is guarded");
        guarded.setCellValueFactory(param->param.getValue().guardedProperty().asString());
        columns.add(guarded);
        TableColumn<ParkingsView, Date> lastControl = new TableColumn<>("Last Control");
        lastControl.setCellValueFactory(param -> param.getValue().lastControlProperty());
        columns.add(lastControl);
        TableColumn<ParkingsView, Float> maxWeight = new TableColumn<>("Max vehicle weight");
        maxWeight.setCellValueFactory(param->param.getValue().maxWeightProperty().asObject());
        columns.add(maxWeight);
        TableColumn<ParkingsView, Float> maxHeight = new TableColumn<>("Max vehicle height");
        maxHeight.setCellValueFactory(param->param.getValue().maxHeightProperty().asObject());
        columns.add(maxHeight);
        TableColumn<ParkingsView, Integer> locationID = new TableColumn<>("Location ID");
        locationID.setCellValueFactory(param->param.getValue().locationIDProperty().asObject());
        columns.add(locationID);
        TableColumn<ParkingsView, String> parkType = new TableColumn<>("Park type");
        parkType.setCellValueFactory(param->param.getValue().parkTypeProperty());
        columns.add(parkType);
        TableColumn<ParkingsView, String> inPaidZone = new TableColumn<>("Is in paid zone");
        inPaidZone.setCellValueFactory(param->param.getValue().zonePaidProperty().asString());
        columns.add(inPaidZone);
        TableColumn<ParkingsView, String> estateName = new TableColumn<>("Estate");
        estateName.setCellValueFactory(param->param.getValue().estateNameProperty());
        columns.add(estateName);
        TableColumn<ParkingsView, String> hasGates = new TableColumn<>("Has gates");
        hasGates.setCellValueFactory(param->param.getValue().gatedProperty().asString());
        columns.add(hasGates);
        TableColumn<ParkingsView, Integer> maxStop = new TableColumn<>("Max stop time");
        maxStop.setCellValueFactory(param->param.getValue().maxStopMinutesProperty().asObject());
        columns.add(maxStop);
        TableColumn<ParkingsView, String> comNode = new TableColumn<>("Communication Node");
        comNode.setCellValueFactory(param->param.getValue().communicationNodeProperty());
        columns.add(comNode);
        TableColumn<ParkingsView, String> isAutomatic = new TableColumn<>("Is automatic");
        isAutomatic.setCellValueFactory(param->param.getValue().automaticProperty().asString());
        columns.add(isAutomatic);
    }


    @FXML
    public void onParkingsFilterClicked() {
        ReportsController.setColumns(parkingsViewTable, columns, columnMenuButton);
        parkingsViewsList=ParkingsViewDAO.getParkingsViews(heightMinInput.getText(), heightMaxInput.getText(), weightMinInput.getText(), weightMaxInput.getText(),
                lotsMinInput.getText(), lotsMaxInput.getText(),
                RootController.selectedMenuItemsToStringList(parkingTypeMenuButton.getItems()),
                roofedCheckBox.isSelected(), guardedCheckBox.isSelected(), freeLotsCheckBox.isSelected());
        parkingsViewTable.setItems(parkingsViewsList);
    }

    private Task<ObservableList<ParkingsView>> generateParkingsViewsLoadTask() {
        Task<ObservableList<ParkingsView>> task = new Task<>() {
            @Override
            protected ObservableList<ParkingsView> call() {
                return ParkingsViewDAO.getParkingsViews(heightMinInput.getText(), heightMaxInput.getText(), weightMinInput.getText(), weightMaxInput.getText(),
                                                        lotsMinInput.getText(), lotsMaxInput.getText(),
                                                        RootController.selectedMenuItemsToStringList(parkingTypeMenuButton.getItems()),
                                                        roofedCheckBox.isSelected(), guardedCheckBox.isSelected(), freeLotsCheckBox.isSelected());
            }
        };

        task.setOnSucceeded(event -> {
            parkingsViewsList.clear();
            parkingsViewsList.addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("SQL Error");
            alert.setHeaderText(event.getSource().getException().getMessage());
            alert.show();
        });

        return task;
    }

    private void scheduleLoadTask(Task task) {
        if(task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
