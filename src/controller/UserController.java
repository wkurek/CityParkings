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
import model.address.AddressDAO;
import model.card.CardDAO;
import model.country.Country;
import model.country.CountryDAO;
import model.user.User;
import model.user.UserDAO;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;
import util.Validator;

import java.io.IOException;
import java.sql.Date;


public class UserController {
    private Stage stage;

    private ObservableList<User> usersList;
    private ObservableList<Vehicle> vehicleList;

    private Task<ObservableList<User>> usersLoadTask;

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> userNameColumn;
    @FXML
    private TableColumn<User, String> userSurnameColumn;
    @FXML
    private TableColumn<User, String> userPhoneNumberColumn;
    @FXML
    private TableColumn<User, String> userCityColumn;
    @FXML
    private TableColumn<User, String> userCountryColumn;

    @FXML
    private Button deleteUserButton;

    @FXML
    private TableView<Vehicle> vehicleTableView;
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
    private ComboBox<Country> userCountryComboBox;

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

    public UserController() {
        usersList = FXCollections.observableArrayList();
        vehicleList = FXCollections.observableArrayList();

        usersLoadTask = generateUsersLoadTask();
    }

    private Task<ObservableList<Vehicle>> generateVehiclesLoadTask(final int userId) {
        Task<ObservableList<Vehicle>> task = new Task<ObservableList<Vehicle>>() {
            @Override
            protected ObservableList<Vehicle> call() {
                return VehicleDAO.getUserVehicles(userId);
            }
        };

        task.setOnSucceeded(event -> {
            vehicleTableView.getSelectionModel().clearSelection();

            vehicleList.clear();
            vehicleList.setAll(task.getValue());
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

    private Task<ObservableList<User>> generateUsersLoadTask() {
        Task<ObservableList<User>> task = new Task<ObservableList<User>>() {
            @Override
            protected ObservableList<User> call() {
                return UserDAO.getUsers();
            }
        };

        task.setOnSucceeded(event -> {
            usersList.clear();
            usersList.addAll(task.getValue());
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
        if(task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }

    private void setUpUsersTable() {
        userIdColumn.setCellValueFactory(param -> param.getValue().idProperty().asObject());
        userNameColumn.setCellValueFactory(param -> param.getValue().nameProperty());
        userSurnameColumn.setCellValueFactory(param -> param.getValue().surnameProperty());
        userPhoneNumberColumn.setCellValueFactory(param -> param.getValue().phoneNumberProperty());
        userCityColumn.setCellValueFactory(param -> param.getValue().getAddress().cityProperty());
        userCountryColumn.setCellValueFactory(param -> param.getValue().getAddress().getCountry().nameProperty());

        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        usersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) onUserSelected(newValue);
        });

        usersTable.setItems(usersList);
    }

    private void setUpVehiclesTable() {
        vehiclePlateNumberColumn.setCellValueFactory(param -> param.getValue().plateNumberProperty());
        vehicleWeightColumn.setCellValueFactory(param -> param.getValue().weightProperty().asObject());
        vehicleHeightColumn.setCellValueFactory(param -> param.getValue().heightProperty().asObject());
        vehicleEngineTypeColumn.setCellValueFactory(param -> param.getValue().getEngine().typeProperty());

        vehicleTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        vehicleTableView.setItems(vehicleList);

        vehicleTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) deleteVehicleButton.setDisable(false);
        });
    }

    private void onUserSelected(User selectedUser) {
        deleteUserButton.setDisable(false);
        saveUserButton.setDisable(true);
        editUserButton.setDisable(false);

        extendCardButton.setDisable(false);

        setUserInputFieldDisable(true);

        deleteVehicleButton.setDisable(true);
        addVehicleButton.setDisable(false);

        userIdInput.setText(Integer.toString(selectedUser.getId()));
        userNameInput.setText(selectedUser.getName());
        userSurnameInput.setText(selectedUser.getSurname());
        userPhoneNumberInput.setText(selectedUser.getPhoneNumber());


        userCityInput.setText(selectedUser.getAddress().getCity());
        userStreetInput.setText(selectedUser.getAddress().getStreet());
        userNumberInput.setText(selectedUser.getAddress().getNumber());
        userZIPCodeInput.setText(selectedUser.getAddress().getZipCode());

        userCountryComboBox.getSelectionModel().select(selectedUser.getAddress().getCountry());

        cardIdInput.setText(Integer.toString(selectedUser.getCard().getCardId()));
        cardExpirationDateInput.setText(selectedUser.getCard().getExpirationDate().toString());

        Task<ObservableList<Vehicle>> vehicleLoadTask = generateVehiclesLoadTask(selectedUser.getId());
        scheduleLoadTask(vehicleLoadTask);

    }

    private void setUserInputFieldDisable(boolean disable) {
        userNameInput.setDisable(disable);
        userSurnameInput.setDisable(disable);
        userPhoneNumberInput.setDisable(disable);
        userCityInput.setDisable(disable);
        userZIPCodeInput.setDisable(disable);
        userStreetInput.setDisable(disable);
        userNumberInput.setDisable(disable);

        userCountryComboBox.setDisable(disable);
    }

    @FXML
    private void initialize () {
        setUpUsersTable();
        setUpVehiclesTable();

        userCountryComboBox.setItems(CountryDAO.getCountries());

        scheduleLoadTask(usersLoadTask);
    }

    @FXML
    public void onRefreshUserButtonClicked() {
        usersLoadTask = generateUsersLoadTask();
        scheduleLoadTask(usersLoadTask);
    }

    @FXML
    public void onNewUserButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserController.class.getResource("../view/newUserView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            NewUserController controller = loader.getController();
            controller.setStage(dialogStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteUserButtonClicked() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        int selectedUserIndex = usersTable.getSelectionModel().getSelectedIndex();

        if(selectedUser != null && selectedUserIndex >= 0) {
            UserDAO.deleteUser(selectedUser.getId());
            usersList.remove(selectedUserIndex);
        } else {
            deleteUserButton.setDisable(true);
        }
    }

    @FXML
    public void onAddVehicleButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserController.class.getResource("../view/newVehicleView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Vehicle");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            NewVehicleController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.setOwner(usersTable.getSelectionModel().getSelectedItem());

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteVehicleButtonClicked() {
        Vehicle selectedVehicle = vehicleTableView.getSelectionModel().getSelectedItem();
        int selectedVehicleIndex = vehicleTableView.getSelectionModel().getSelectedIndex();

        if(selectedVehicle != null && selectedVehicleIndex >= 0) {
            VehicleDAO.deleteVehicle(selectedVehicle.getId());
            vehicleList.remove(selectedVehicleIndex);
        } else {
            deleteVehicleButton.setDisable(true);
        }
    }

    @FXML
    public void onEditUserButtonClicked() {
        editUserButton.setDisable(true);
        saveUserButton.setDisable(false);

        setUserInputFieldDisable(false);
    }

    private String getFieldsValidationString() {
        StringBuilder stringBuilder = new StringBuilder();

        if(!Validator.isNameValid(userNameInput.getText())) {
            stringBuilder.append("Invalid name.");
        } else  if(!Validator.isSurnameValid(userSurnameInput.getText())) {
            stringBuilder.append("Invalid surname.");
        } else if(!Validator.isPhoneNumberValid(userPhoneNumberInput.getText())) {
            stringBuilder.append("Invalid phone number.");
        } else if(!Validator.isCityValid(userCityInput.getText())) {
            stringBuilder.append("Invalid city name.");
        } else if(!Validator.isStreetValid(userStreetInput.getText())) {
            stringBuilder.append("Invalid street name.");
        } else if(!Validator.isNumberValid(userNumberInput.getText())) {
            stringBuilder.append("Invalid home number.");
        } else if(!Validator.isZIPCodeValid(userZIPCodeInput.getText())) {
            stringBuilder.append("Invalid zip code.");
        }

        return stringBuilder.toString();
    }


    private boolean areEditedFieldsValid() {
        return Validator.isNameValid(userNameInput.getText()) && Validator.isSurnameValid(userSurnameInput.getText()) &&
                Validator.isPhoneNumberValid(userPhoneNumberInput.getText()) &&
                Validator.isCityValid(userCityInput.getText()) && Validator.isStreetValid(userStreetInput.getText()) &&
                Validator.isNumberValid(userNumberInput.getText()) && Validator.isZIPCodeValid(userZIPCodeInput.getText());
    }

    @FXML
    public void onSaveUserButtonClicked() {
        if(areEditedFieldsValid()) {
            int editedUserIndex = usersTable.getSelectionModel().getSelectedIndex();
            User editedUser = usersList.get(editedUserIndex);

            editedUser.setName(userNameInput.getText());
            editedUser.setSurname(userSurnameInput.getText());
            editedUser.setPhoneNumber(userPhoneNumberInput.getText());

            editedUser.getAddress().setCity(userCityInput.getText());
            editedUser.getAddress().setZipCode(userZIPCodeInput.getText());
            editedUser.getAddress().setStreet(userStreetInput.getText());
            editedUser.getAddress().setNumber(userNumberInput.getText());

            Country selectedCountry = userCountryComboBox.getSelectionModel().getSelectedItem();
            editedUser.getAddress().getCountry().setName(selectedCountry.getName());
            editedUser.getAddress().getCountry().setIso(selectedCountry.getIso());


            AddressDAO.updateAddress(editedUser.getAddress().getId(), editedUser.getAddress());
            UserDAO.updateUser(editedUser.getId(), editedUser);

            editUserButton.setDisable(false);
            saveUserButton.setDisable(true);
            setUserInputFieldDisable(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid field(s)");
            alert.setContentText(getFieldsValidationString());
            alert.show();
        }
    }


    @FXML
    public void onExtendCardButtonClicked() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();

        if(selectedUser != null && selectedUser.getCard() != null) {
            Date date = selectedUser.getCard().getExpirationDate();

            selectedUser.getCard().setExpirationDate(Date.valueOf(date.toLocalDate().plusYears(1)));
            cardExpirationDateInput.setText(selectedUser.getCard().getExpirationDate().toString());

            CardDAO.updateCard(selectedUser.getCard().getCardId(), selectedUser.getCard());
        }
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
