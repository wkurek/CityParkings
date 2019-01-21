package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.park.Park;
import model.park.ParkDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.Validator;

import java.io.IOException;
import java.sql.Date;

public class ParkingController {
    public Button deleteParkingButton;
    public TextField parkingIsGuardedInput;
    public TextField parkingIsRoofedInput;
    public TextField parkingLastControlInput;
    private ObservableList<Parking> parkingList;
    private ObservableList<Park> parksList;

    private Stage stage;
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
    private TextField parkingIdInput;

    @FXML
    private TextField parkingStandardSlotsNumberInput;

    @FXML
    private TextField parkingDisabledLotsNumberInput;


    @FXML
    private TextField maxWeightInput;

    @FXML
    private TextField maxHeightInput;

    @FXML
    private TextField locationInput;

    @FXML
    private Button editParkingButton;

    @FXML
    private Button saveParkingButton;


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
        deleteParkingButton.setDisable(true);

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
        });
    }

    private void onParkingSelected(Parking selectedParking) {
        saveParkingButton.setDisable(true);
        editParkingButton.setDisable(false);
        deleteParkingButton.setDisable(false);

        setParkingInputFieldDisable(true);

        parkingIdInput.setText(Integer.toString(selectedParking.getParkingId()));
        parkingStandardSlotsNumberInput.setText(Integer.toString(selectedParking.getStandardLots()));
        parkingDisabledLotsNumberInput.setText(Integer.toString(selectedParking.getDisabledLots()));
        parkingIsRoofedInput.setText(Boolean.toString(selectedParking.isRoofed()));
        parkingIsGuardedInput.setText(Boolean.toString(selectedParking.isGuarded()));
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
        parkingIsGuardedInput.setDisable(disable);
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
    void onEditParkingButtonClicked() {
        editParkingButton.setDisable(true);
        saveParkingButton.setDisable(false);
        deleteParkingButton.setDisable(true);

        setParkingInputFieldDisable(false);

        parkingIdInput.setDisable(true);
        locationInput.setDisable(true);
    }

    @FXML
    void onNewParkingButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ParkingController.class.getResource("../view/newParkingView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Parking");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            NewParkingController controller = loader.getController();
            controller.setStage(dialogStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSaveParkingButtonClicked() {

        if(areInputFieldsValid()) {
            int editedParkingIndex = parkingsTable.getSelectionModel().getSelectedIndex();
            Parking editParking = parkingList.get(editedParkingIndex);

            editParking.setStandardLots(Integer.valueOf(parkingStandardSlotsNumberInput.getText()));
            editParking.setDisabledLots(Integer.valueOf(parkingDisabledLotsNumberInput.getText()));
            editParking.setRoofed(Boolean.getBoolean(parkingIsRoofedInput.getText()));
            editParking.setGuarded(Boolean.getBoolean(parkingIsGuardedInput.getText()));
            editParking.setLastControl(Date.valueOf(parkingLastControlInput.getText()));

            editParking.setMaxHeight(Float.valueOf(maxHeightInput.getText()));
            editParking.setMaxWeight(Float.valueOf(maxWeightInput.getText()));

            editParkingButton.setDisable(false);
            saveParkingButton.setDisable(true);
            deleteParkingButton.setDisable(true);
            setParkingInputFieldDisable(true);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid field(s)");
            alert.show();
        }

    }

    private boolean areInputFieldsValid() {
        boolean isOK = Validator.isIntegerInputValid(parkingStandardSlotsNumberInput.getText()) && Validator.isIntegerInputValid(parkingDisabledLotsNumberInput.getText())
                && Validator.isWeightValid(maxWeightInput.getText()) && Validator.isHeightValid(maxHeightInput.getText())
                && Validator.isDateValid(parkingLastControlInput.getText());
        return isOK;
    }
    public void onDeleteParkingButtonClicked() {
        Parking selectedParking = parkingsTable.getSelectionModel().getSelectedItem();
        int selectedParkingIndex = parkingsTable.getSelectionModel().getSelectedIndex();

        if(selectedParking != null && selectedParkingIndex >= 0) {
            ParkingDAO.deleteParking(selectedParking.getParkingId());
            parkingList.remove(selectedParkingIndex);
        } else {
            deleteParkingButton.setDisable(true);
        }
    }

    public void onRefreshParkingButtonClicked() {
        scheduleLoadTask(parkingLoadTask);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
