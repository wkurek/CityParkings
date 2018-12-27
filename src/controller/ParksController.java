package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.address.Address;

public class ParksController {

    @FXML
    private Pagination parkingsPagination;

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
    private ChoiceBox<?> parkIsRoofedChoiceBox;

    @FXML
    private ChoiceBox<?> parkIsGuardedChoiceBox;

    @FXML
    private TextField parkMaxWeightInput;

    @FXML
    private TextField parkMaxHeightInput;

    @FXML
    private TextField parkParkLatitudeInput;

    @FXML
    private TextField parkLongitudeInput;

    @FXML
    void onEditParkButtonClicked(ActionEvent event) {

    }

    @FXML
    void onNewParkButtonClicked(ActionEvent event) {

    }

    @FXML
    void onSaveParkButtonClicked(ActionEvent event) {

    }

}
