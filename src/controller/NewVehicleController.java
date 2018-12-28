package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.engine.Engine;
import model.engine.EngineDAO;
import model.user.User;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;
import util.Validator;


public class NewVehicleController {
    private Stage stage;
    private User owner;

    @FXML
    private TextField plateNumberInput;
    @FXML
    private TextField weightInput;
    @FXML
    private TextField heightInput;

    private Task<Void> saveVehicleTask;

    @FXML
    private ComboBox<Engine> engineTypeComboBox;


    @FXML
    private void initialize() {
        engineTypeComboBox.setItems(EngineDAO.getEngines());

        saveVehicleTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Vehicle vehicle = new Vehicle();

                vehicle.setUser(owner);
                vehicle.setEngine(engineTypeComboBox.getSelectionModel().getSelectedItem());
                vehicle.setPlateNumber(plateNumberInput.getText());

                    float height = Float.parseFloat(heightInput.getText());
                    float weight = Float.parseFloat(weightInput.getText());

                vehicle.setHeight(height);
                vehicle.setWeight(weight);

                VehicleDAO.saveVehicle(vehicle);

                return null;
            }
        };

        saveVehicleTask.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("SQL Error");
            alert.setHeaderText(event.getSource().getException().getMessage());
            alert.show();
        });
    }

    private void scheduleSaveTask() {
        new Thread(saveVehicleTask).start();
    }

    private boolean areInputFieldsValid() {
        return Validator.isPlateNumberValid(plateNumberInput.getText()) &&
                Validator.isWeightValid(weightInput.getText()) && Validator.isHeightValid(heightInput.getText())
                && engineTypeComboBox.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private void onSaveClicked() {
        if(areInputFieldsValid() && owner != null) {
            scheduleSaveTask();
            onCancelClicked();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid field(s)");
            alert.show();
        }
    }

    @FXML
    private void onCancelClicked() {
        if(stage != null) stage.close();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    void setOwner(User user) {
        this.owner = user;
    }

}
