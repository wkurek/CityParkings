package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.park.Park;
import model.park.ParkDAO;
import org.joda.time.DateTime;


import java.io.IOException;

public class ParkController {


    private ObservableList<Park> parksList;
    private Task<ObservableList<Park>> parksLoadTask;
    @FXML
    private Button deleteParkButton;

    @FXML
    private TableView<Park> parksTable;

    @FXML
    private TableColumn<Park, DateTime> parkDateColumn;

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

    public TextField parkingLocationInput;
    @FXML
    private TextField parkingIsGuardedInput;
    @FXML
    private TextField parkingIsRoofedInput;
    @FXML
    private TextField parkingStandardLotsNumberInput;
    @FXML
    private TextField parkingDisabledLotsNumberInput;
    @FXML
    private TextField parkingLastControlInput;
    @FXML
    private TextField parkingMaxWeightInput;
    @FXML
    private TextField parkingMaxHeightInput;

    public TextField parkVehiclePlateNumberInput;
    public TextField parkVehicleWeightInput;
    public TextField parkVehicleHeightInput;
    public TextField parkVehicleUserIdInput;

    private Stage stage;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("SQL Error");
            alert.setHeaderText(event.getSource().getException().getMessage());
            alert.show();
        });

        return task;
    }

    private void scheduleLoadTask(Task task) {
        if (task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }

    private void setUpParksTable() {
        ReportsController.setParkingDateColumn(parkDateColumn);
        parkDateColumn.setCellValueFactory(param -> param.getValue().getTimeProperty());
        parkVehicleIdColumn.setCellValueFactory(param -> param.getValue().getVehicle().idProperty().asObject());
        parkParkingIdColumn.setCellValueFactory(param -> param.getValue().getParking().parkingIdProperty().asObject());

        parksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        parksTable.setItems(parksList);

        parksTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) onParkSelected(newValue);
        });

    }

    @FXML
    private void initialize() {
        setUpParksTable();
        scheduleLoadTask(parksLoadTask);
    }

    @FXML
    private void onParkSelected(Park selectedPark) {

        deleteParkButton.setDisable(false);

        parkVehicleIdInput.setText(Integer.toString(selectedPark.getVehicle().getId()));
        parkParkingIdInput.setText(Integer.toString(selectedPark.getParking().getParkingId()));
        parkDateInput.setText(selectedPark.getDateTime().toString());

        parkingStandardLotsNumberInput.setText(String.valueOf(selectedPark.getParking().getStandardLots()));
        parkingDisabledLotsNumberInput.setText(String.valueOf(selectedPark.getParking().getDisabledLots()));
        parkingIsGuardedInput.setText(Boolean.toString(selectedPark.getParking().isGuarded()));
        parkingIsRoofedInput.setText(Boolean.toString(selectedPark.getParking().isRoofed()));
        parkingLastControlInput.setText(selectedPark.getParking().getLastControl().toString());
        parkingMaxHeightInput.setText(Float.toString(selectedPark.getParking().getMaxHeight()));
        parkingMaxWeightInput.setText(Float.toString(selectedPark.getParking().getMaxWeight()));
        parkingLocationInput.setText(Float.toString(selectedPark.getParking().getLocation().getLatitude()) +
                ", " + Float.toString(selectedPark.getParking().getLocation().getLongitude()));

        parkVehiclePlateNumberInput.setText(selectedPark.getVehicle().getPlateNumber());
        parkVehicleWeightInput.setText(Float.toString(selectedPark.getVehicle().getWeight()));
        parkVehicleHeightInput.setText(Float.toString(selectedPark.getVehicle().getHeight()));
        parkVehicleUserIdInput.setText(Integer.toString(selectedPark.getVehicle().getUser().getId()));

    }

    @FXML
    void onDeleteParkButtonClicked(ActionEvent event) {
        Park selectedPark = parksTable.getSelectionModel().getSelectedItem();
        int selectedParkIndex = parksTable.getSelectionModel().getFocusedIndex();

        if(selectedPark != null && selectedParkIndex >= 0) {
            ParkDAO.deletePark(selectedPark.getDateTime().toString(), selectedPark.getVehicle().getId());
            parksList.remove(selectedParkIndex);
        } else {
            deleteParkButton.setDisable(true);
        }

    }

    @FXML
    void onNewParkButtonClicked(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ParkController.class.getResource("../view/newParkView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Park");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            NewParkController controller = loader.getController();
            controller.setStage(dialogStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRefreshParkButtonClicked(ActionEvent event) {
        parksLoadTask = generateParksLoadTask();
        scheduleLoadTask(parksLoadTask);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}