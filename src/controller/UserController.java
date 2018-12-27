package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.vehicle.Vehicle;

public class UserController {
    @FXML
    private Pagination userPagination;

    @FXML
    private Button refreshUserButton;
    @FXML
    private Button newUserButton;
    @FXML
    private Button deleteUserButton;

    @FXML
    private TableColumn<Vehicle, String> vehiclePlateNumberColumn;
    @FXML
    private TableColumn<Vehicle, Float> vehicleWeightColumn;
    @FXML
    private TableColumn<Vehicle, Float> vehicleHeightColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleEngineTypeColumn;

    @FXML
    private Button addVehicleButton;
    @FXML
    private Button deleteVehicleButton;

    @FXML
    private TextField userIdInput;
    @FXML
    private TextField userNameInput;
    @FXML
    private TextField userSurnameInput;
    @FXML
    private TextField userPhoneNumberInput;
    @FXML
    private TextField userCityInput;
    @FXML
    private TextField userStreetInput;
    @FXML
    private TextField userZIPCodeInput;
    @FXML
    private TextField userNumberInput;
    
    @FXML
    private ChoiceBox userCountryChoiceBox;

    
    @FXML
    private Button editUserButton;
    @FXML
    private Button saveUserButton;

    @FXML
    private TextField cardIdInput;
    @FXML
    private TextField cardExpirationDateInput;

    @FXML
    private Button extendCardButton;
    @FXML
    private Button generateCardButton;
    @FXML
    private Button deleteCardButton;

    @FXML
    private void initialize () {

    }

    @FXML
    public void onRefreshUserButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onNewUserButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onDeleteUserButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onAddVehicleButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onDeleteVehicleButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onEditUserButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onSaveUserButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onExtendCardButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onGenerateCardButtonClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onDeleteCardButtonClicked(ActionEvent actionEvent) {
    }
}
