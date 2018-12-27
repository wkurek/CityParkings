package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.address.AddressDAO;
import model.card.CardDAO;
import model.country.Country;
import model.country.CountryDAO;
import model.user.User;
import model.user.UserDAO;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;

import java.sql.Date;


public class UserController {
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
            //TODO: show alert
            System.err.println(task.getException().getMessage());
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
            //TODO: show alert
            System.err.println(task.getException().getMessage());
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
        scheduleLoadTask(usersLoadTask);
    }

    @FXML
    public void onNewUserButtonClicked() {
        //TODO: invoke popup with UserForm
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

    @FXML
    public void onSaveUserButtonClicked() {
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
}