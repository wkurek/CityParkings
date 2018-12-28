package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.park.Park;
import model.park.ParkDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import model.user.User;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;

public class ParkingController {
    private ObservableList<Parking> parkingList;
    private ObservableList<Park> parksList;

    private Task<ObservableList<Parking>> parkingLoadTask;

    @FXML
    public TableView<Parking> parkingsTable;

    @FXML
    public TableView<Park> parksTable;

    @FXML
    private TableColumn<Parking, Integer> parkingIdColumn;

    @FXML
    private TableColumn<Parking, Integer> parkingStandardLotsNumberColumn;

    @FXML
    private TableColumn<Parking, Integer> parkingDisabledLotsNumerColumn;

    @FXML
    private TableColumn<Parking, String > parkingLastControlColumn;

    @FXML
    private TableColumn<Parking, Float> parkingMaxWeightColumn;

    @FXML
    private TableColumn<Parking, Float> parkingsMaxHeightColumn;

    //Parks table
    @FXML
    private TableColumn<Park, Integer> parkVehicleIdColumn;

    @FXML
    private TableColumn<Park, String> parkDateColumn;

    @FXML
    private Button addParkButton;

    @FXML
    private Button deleteParkButton;

    @FXML
    private TextField parkingIdInput;

    @FXML
    private TextField parkingStandardSlotsNumberInput;

    @FXML
    private TextField parkingDisabledLotsNumberInput;

    @FXML
    private ChoiceBox<?> isRoofedChoiceBox;

    @FXML
    private ChoiceBox<?> isGuardedChoiceBox;

    @FXML
    private DatePicker lastControlDatePicker;

    @FXML
    private TextField maxWeightInput;

    @FXML
    private TextField maxHeightInput;

    @FXML
    private TextField locationInput;

    @FXML
    private TextField col1Input;

    @FXML
    private TextField col2Input;

    @FXML
    private Button editParkingButton;

    @FXML
    private Button saveParkingButton;

    @FXML
    private Button newParkingButton;

    @FXML
    private TextField employeeIdInput;

    @FXML
    private TextField employeeNameInput;

    @FXML
    private TextField employeeSurnameInput;

    @FXML
    private TextField employeeDepartmentNameInput;

    public ParkingController() {
        parkingList = FXCollections.observableArrayList();
        parksList = FXCollections.observableArrayList();

        parkingLoadTask = generateParkingLoadTask();

    }

    private Task<ObservableList<Park>> generateParksLoadTask(final int parkingId) {
        Task<ObservableList<Park>> task = new Task<ObservableList<Park>>() {
            @Override
            protected ObservableList<Park> call() {
                return ParkDAO.getParkingParks(parkingId);
            }
        };

        task.setOnSucceeded(event -> {
            parksTable.getSelectionModel().clearSelection();

            parksList.clear();
            parksList.setAll(task.getValue());
        });

        task.setOnFailed(event -> {
            //TODO: show alert
            System.err.println(task.getException().getMessage());
        });

        return task;
    }


    private Task<ObservableList<Parking>> generateParkingLoadTask() {
        Task<ObservableList<Parking>> task = new Task<ObservableList<Parking>>() {
            @Override
            protected ObservableList<Parking> call() {
                return ParkingDAO.getParkings();
            }
        };

        task.setOnSucceeded(event -> {
            parkingList.clear();
            parkingList.setAll(task.getValue());
        });

        task.setOnFailed(event -> {
            //TODO: show alert
            System.err.println(task.getException().getMessage());
        });

        return task;
    }

    @FXML
    private void initialize() {
        setUpParkingsTable();
        setUpParksTable();

        scheduleLoadTask(parkingLoadTask);

    }

    private void setUpParkingsTable() {
        parkingIdColumn.setCellValueFactory(param -> param.getValue().parkingIdProperty().asObject());
        parkingDisabledLotsNumerColumn.setCellValueFactory(param -> param.getValue().disabledLotsProperty().asObject());
        parkingLastControlColumn.setCellValueFactory(param -> param.getValue().lastControlProperty().asString());
        parkingStandardLotsNumberColumn.setCellValueFactory(param -> param.getValue().standardLotsProperty().asObject());
        parkingMaxWeightColumn.setCellValueFactory(param -> param.getValue().maxWeightProperty().asObject());
        parkingsMaxHeightColumn.setCellValueFactory(param -> param.getValue().maxHeightProperty().asObject());

        parkingsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        parkingsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) onParkingSelected(newValue);
        });

        parkingsTable.setItems(parkingList);
    }

    private void setUpParksTable() {
        parkVehicleIdColumn.setCellValueFactory(param -> param.getValue().getVehicle().idProperty().asObject());
        parkDateColumn.setCellValueFactory(param -> param.getValue().dateTimeProperty().asString());

        parksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        parksTable.setItems(parksList);

        parksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //if(newValue != null) deleteVehicleButton.setDisable(false);
        });
    }

    private void onParkingSelected(Parking selectedParking) {
        Task<ObservableList<Park>> parkingLoadTask = generateParksLoadTask(selectedParking.getParkingId());
        scheduleLoadTask(parkingLoadTask);
    }

    private void scheduleLoadTask(Task task) {
        if(task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }

    @FXML
    void onAddParkButtonClicked(ActionEvent event) {

    }

    @FXML
    void onDeleteParkButtonClicked(ActionEvent event) {

    }

    @FXML
    void onEditParkingButtonClicked(ActionEvent event) {

    }

    @FXML
    void onNewParkingButtonClicked(ActionEvent event) {

    }

    @FXML
    void onSaveParkingButtonClicked(ActionEvent event) {

    }

}
