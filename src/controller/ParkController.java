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
import model.parking.Parking;
import model.parking.ParkingDAO;
import model.user.User;
import model.user.UserDAO;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;

import java.sql.Date;

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

    @FXML
    private void onParkSelected(Park selectedPark) {
        deleteParkButton.setDisable(false);
        saveParkButton.setDisable(true);
        editParkButton.setDisable(false);

        parkVehicleIdInput.setText(Integer.toString(selectedPark.getVehicle().getId()));
        parkParkingIdInput.setText(Integer.toString(selectedPark.getParking().getParkingId()));
        parkDateInput.setText(selectedPark.getDateTime().toString());

        parkStandardLotsNumerInput.setText(String.valueOf(selectedPark.getParking().getStandardLots()));
        parkDisabledParkLotsInput.setText(String.valueOf(selectedPark.getParking().getDisabledLots()));
        parkingIsGuardedInput.setText(Boolean.toString(selectedPark.getParking().isGuarded()));
        parkingIsRoofedInput.setText(Boolean.toString(selectedPark.getParking().isRoofed()));
        parkMaxHeightInput.setText(Float.toString(selectedPark.getParking().getMaxHeight()));
        parkMaxWeightInput.setText(Float.toString(selectedPark.getParking().getMaxWeight()));
        parkLongitudeInput.setText(Float.toString(selectedPark.getParking().getLocation().getLatitude()));
        parkParkLatitudeInput.setText(Float.toString(selectedPark.getParking().getLocation().getLatitude()));
    }

    @FXML
    void onDeleteParkButtonClicked(ActionEvent event) {
        Park selectedPark = parksTableView.getSelectionModel().getSelectedItem();
        int selectedParkIndex = parksTableView.getSelectionModel().getFocusedIndex();

        if(selectedPark != null && selectedParkIndex >= 0) {
            ParkDAO.deletePark(selectedPark.getDateTime().toString(), selectedPark.getVehicle().getId());
            parksList.remove(selectedParkIndex);
        } else {
            deleteParkButton.setDisable(true);
        }
    }

    private void setParkInputFieldDistable(boolean distable) {
        parkVehicleIdInput.setDisable(distable);
        parkParkingIdInput.setDisable(distable);
        parkDateInput.setDisable(distable);
    }

    @FXML
    void onEditParkButtonClicked(ActionEvent event) {
        editParkButton.setDisable(true);
        saveParkButton.setDisable(false);

        setParkInputFieldDistable(false);
    }

    @FXML
    void onNewParkButtonClicked(ActionEvent event) {
        //TODO: invoke popup with UserForm
    }

    @FXML
    void onRefreshButtonClicked(ActionEvent event) {
        scheduleLoadTask(parksLoadTask);
    }

    @FXML
    void onSaveParkButtonClicked(ActionEvent event) {
        int editedParkIndex = parksTableView.getSelectionModel().getSelectedIndex();
        Park editedPark = parksList.get(editedParkIndex);

        Vehicle editedVehicle = VehicleDAO.getVehicle(Integer.valueOf(parkVehicleIdInput.getText()));
        Parking editedParking = ParkingDAO.getParking(Integer.valueOf(parkParkingIdInput.getText()));
        editedPark.setVehicle(editedVehicle);
        editedPark.setParking(editedParking);
        editedPark.setDateTime(Date.valueOf(parkDateInput.getText()));

        editParkButton.setDisable(false);
        saveParkButton.setDisable(true);
        setParkInputFieldDistable(true);

    }

}
