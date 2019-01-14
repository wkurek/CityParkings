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

import java.sql.Date;

public class ParkingController {
    public Button deleteParkingButton;
    public TextField parkingIsGuardeInput;
    public TextField parkingIsRoofedInput;
    public TextField parkingLastControlInput;
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
        saveParkingButton.setDisable(true);
        editParkingButton.setDisable(false);

        setParkingInputFieldDisable(true);

        parkingIdInput.setText(Integer.toString(selectedParking.getParkingId()));
        parkingStandardSlotsNumberInput.setText(Integer.toString(selectedParking.getStandardLots()));
        parkingDisabledLotsNumberInput.setText(Integer.toString(selectedParking.getDisabledLots()));
        parkingIsRoofedInput.setText(Boolean.toString(selectedParking.isRoofed()));
        parkingIsGuardeInput.setText(Boolean.toString(selectedParking.isGuarded()));
        parkingLastControlInput.setText(selectedParking.getLastControl().toString());
        maxHeightInput.setText(Float.toString(selectedParking.getMaxHeight()));
        maxWeightInput.setText(Float.toString(selectedParking.getMaxWeight()));
        locationInput.setText(Float.toString(selectedParking.getLocation().getLatitude()) +
                ", " + Float.toString(selectedParking.getLocation().getLongitude()));

        Task<ObservableList<Park>> parkingLoadTask = generateParksLoadTask(selectedParking.getParkingId());
        scheduleLoadTask(parkingLoadTask);
    }

    private void setParkingInputFieldDisable(boolean disable) {
        parkingIdInput.setDisable(disable);
        parkingStandardSlotsNumberInput.setDisable(disable);
        parkingDisabledLotsNumberInput.setDisable(disable);
        parkingIsRoofedInput.setDisable(disable);
        parkingIsGuardeInput.setDisable(disable);
        parkingLastControlInput.setDisable(disable);
        maxWeightInput.setDisable(disable);
        maxHeightInput.setDisable(disable);
        locationInput.setDisable(disable);

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
        Park selectedPark = parksTable.getSelectionModel().getSelectedItem();
        int selectedParkIndex = parksTable.getSelectionModel().getSelectedIndex();

        if(selectedPark != null && selectedParkIndex >= 0) {
            ParkDAO.deletePark(selectedPark.getDateTime().toString(), selectedPark.getVehicle().getId());
            parksList.remove(selectedParkIndex);
        } else {
            deleteParkButton.setDisable(true);
        }
    }

    @FXML
    void onEditParkingButtonClicked(ActionEvent event) {
        editParkingButton.setDisable(true);
        saveParkingButton.setDisable(false);

        setParkingInputFieldDisable(false);

        parkingIdInput.setDisable(true);
        locationInput.setDisable(true);
    }

    @FXML
    void onNewParkingButtonClicked(ActionEvent event) {

    }

    @FXML
    void onSaveParkingButtonClicked(ActionEvent event) {

        int editedParkingIndex = parkingsTable.getSelectionModel().getSelectedIndex();
        Parking editParking = parkingList.get(editedParkingIndex);

        editParking.setStandardLots(Integer.valueOf(parkingStandardSlotsNumberInput.getText()));
        editParking.setDisabledLots(Integer.valueOf(parkingDisabledLotsNumberInput.getText()));
        editParking.setRoofed(Boolean.getBoolean(parkingIsRoofedInput.getText()));
        editParking.setGuarded(Boolean.getBoolean(parkingIsGuardeInput.getText()));
        editParking.setLastControl(Date.valueOf(parkingLastControlInput.getText()));

        editParking.setMaxHeight(Float.valueOf(maxHeightInput.getText()));
        editParking.setMaxWeight(Float.valueOf(maxWeightInput.getText()));

        editParkingButton.setDisable(false);
        saveParkingButton.setDisable(true);
        setParkingInputFieldDisable(true);

    }

    public void onDeleteParkingButtonClicked(ActionEvent actionEvent) {
        Parking selectedParking = parkingsTable.getSelectionModel().getSelectedItem();
        int selectedParkingIndex = parkingsTable.getSelectionModel().getSelectedIndex();

        if(selectedParking != null && selectedParkingIndex >= 0) {
            ParkingDAO.deleteParking(selectedParking.getParkingId());
            parkingList.remove(selectedParkingIndex);
        } else {
            deleteParkingButton.setDisable(true);
        }
    }

    public void onRefreshParkingButtonClicked(ActionEvent actionEvent) {
        scheduleLoadTask(parkingLoadTask);
    }
}
