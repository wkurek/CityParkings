package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class ParkingController {

    @FXML
    private TableColumn<?, ?> parkingIdColumn;

    @FXML
    private TableColumn<?, ?> parkVehiclePlateNumberColumn;

    @FXML
    private TableColumn<?, ?> parkDateColumn;

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
