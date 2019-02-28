package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.park.Park;
import model.parking.Parking;
import model.parking.ParkingDAO;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;

import java.sql.SQLException;

public class NewParkController {

    private Stage stage;
    @FXML
    private ComboBox<Vehicle> vehicleIdComboBox;
    @FXML
    private ComboBox<Parking> parkingIdComboBox;

    private Task<Void> saveParkTask;

    @FXML
    private void initialize() {

        vehicleIdComboBox.setItems(VehicleDAO.getVehicles());
        parkingIdComboBox.setItems(ParkingDAO.getParkings());

        saveParkTask = new Task<Void>() {
            @Override
            protected Void call() throws SQLException {

                Parking selectedParking = parkingIdComboBox.getSelectionModel().getSelectedItem();
                Vehicle selectedVehicle = vehicleIdComboBox.getSelectionModel().getSelectedItem();

                Park park = new Park();
                park.setParking(selectedParking);
                park.setVehicle(selectedVehicle);

                return null;
            }
        };

        saveParkTask.setOnFailed(event -> {
            ReportsController.taskAlert(stage, event);
        });
    }

    private void scheduleSaveTask() {
        new Thread(saveParkTask).start();
    }

    public void onSaveButtonClicked() {
        scheduleSaveTask();
        onCancelButtonClicked();
    }

    public void onCancelButtonClicked() {
        if(stage != null) stage.close();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
