package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.park.Park;
import model.park.ParkDAO;
import model.user.User;
import model.user.UserDAO;

public class ParkController {
    private ObservableList<Park> parksList;
    private Task<ObservableList<Park>> parksLoadTask;
    @FXML
    private Button deleteParkButton;

    @FXML
    private TableView<Park> parksTableView;

    @FXML
    private TableColumn<Park, String> parkDateColumn;

    @FXML
    private TableColumn<Park, Integer> parkVehicleIdColumn;

    @FXML
    private TableColumn<Park, Integer> parkParkingIdColumn;

    @FXML
    private TextField parkDateInput;

    @FXML
    private TextField parkVehicleIdInput;

    @FXML
    private TextField parkParkingIdInput;

    @FXML
    private Button editParkButton;

    @FXML
    private Button saveParkButton;

    @FXML
    private Button newParkButton;

    @FXML
    private TextField parkVehiclePlateNumerInput;

    @FXML
    private TextField parkVehicleWeightInput;

    @FXML
    private TextField parkVehicleHeightInput;

    @FXML
    private TextField parkVehicleUserIdInput;

    @FXML
    private TextField parkStandardLotsNumerInput;

    @FXML
    private TextField parkDisabledParkLotsInput;

    @FXML
    private TextField parkingIsRoofedInput;

    @FXML
    private TextField parkingIsGuardedInput;

    @FXML
    private TextField parkMaxWeightInput;

    @FXML
    private TextField parkMaxHeightInput;

    @FXML
    private TextField parkParkLatitudeInput;

    @FXML
    private TextField parkLongitudeInput;

    public ParkController(){
        parksList= FXCollections.observableArrayList();
        parksLoadTask = generateParksLoadTask();
    }

    private Task<ObservableList<Park>> generateParksLoadTask() {
        Task<ObservableList<Park>> task = new Task<ObservableList<Park>>() {
            @Override
            protected ObservableList<Park> call() {
                return ParkDAO.getParks();
            }
        };

        task.setOnSucceeded(event -> {
            parksList.clear();
            parksList.addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            //TODO: show alert
            System.err.println(task.getException().getMessage());
        });

        return task;
    }

    private void scheduleLoadTask(Task task) {
        if (task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }

    private void setUpParksTable() {
        parkDateColumn.setCellValueFactory(param -> param.getValue().dateTimeProperty().asString());
        parkVehicleIdColumn.setCellValueFactory(param -> param.getValue().getVehicle().idProperty().asObject());
        parkParkingIdColumn.setCellValueFactory(param -> param.getValue().getParking().parkingIdProperty().asObject());

        parksTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        parksTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) onParkSelected(newValue);
        });

        parksTableView.setItems(parksList);
    }

    @FXML
    private void initialize() {
        setUpParksTable();
        scheduleLoadTask(parksLoadTask);
    }

    private void onParkSelected(Park selectedPark) {

    }

    @FXML
    void onDeleteParkButtonClicked(ActionEvent event) {

    }

    @FXML
    void onEditParkButtonClicked(ActionEvent event) {

    }

    @FXML
    void onNewParkButtonClicked(ActionEvent event) {

    }

    @FXML
    void onRefreshButtonClicked(ActionEvent event) {

    }

    @FXML
    void onSaveParkButtonClicked(ActionEvent event) {

    }

}
