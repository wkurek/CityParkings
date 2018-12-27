package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.address.Address;
import model.card.Card;
import model.card.CardDAO;
import model.country.Country;
import model.country.CountryDAO;
import model.user.User;
import model.user.UserDAO;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;


public class UserController {
    private static final int PAGINATION_USERS_PER_PAGE_NUMBER = 8;
    private static final int PAGINATION_USERS_INIT_PAGE_NUMBER = 0;

    private static final String USERS_TABLE_COLUMN_NAME_ID = "Id";
    private static final String USERS_TABLE_COLUMN_NAME_NAME = "Name";
    private static final String USERS_TABLE_COLUMN_NAME_SURNAME = "Surname";
    private static final String USERS_TABLE_COLUMN_NAME_PHONE_NUMBER = "Phone number";
    private static final String USERS_TABLE_COLUMN_NAME_CITY = "City";
    private static final String USERS_TABLE_COLUMN_NAME_COUNTRY = "Country";

    private ObservableList<User> usersList;
    private ObservableList<Vehicle> vehicleList;

    private Task<ObservableList<User>> usersLoadTask;
    private Task<ObservableList<Vehicle>> vehicleLoadTask;

    private TableView<User> usersTable;

    @FXML
    private Pagination userPagination;

    @FXML
    private Button newUserButton;
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
            setUpUserPagination();
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

    private void generateUsersTable() {
        usersTable = new TableView<>();

        TableColumn<User, Integer> idColumn = new TableColumn<>(USERS_TABLE_COLUMN_NAME_ID);
        idColumn.setCellValueFactory(param -> param.getValue().idProperty().asObject());

        TableColumn<User, String> nameColumn = new TableColumn<>(USERS_TABLE_COLUMN_NAME_NAME);
        nameColumn.setCellValueFactory(param -> param.getValue().nameProperty());

        TableColumn<User, String> surnameColumn = new TableColumn<>(USERS_TABLE_COLUMN_NAME_SURNAME);
        surnameColumn.setCellValueFactory(param -> param.getValue().surnameProperty());

        TableColumn<User, String> phoneNumberColumn = new TableColumn<>(USERS_TABLE_COLUMN_NAME_PHONE_NUMBER);
        phoneNumberColumn.setCellValueFactory(param -> param.getValue().phoneNumberProperty());

        TableColumn<User, String> cityColumn = new TableColumn<>(USERS_TABLE_COLUMN_NAME_CITY);
        cityColumn.setCellValueFactory(param -> param.getValue().getAddress().cityProperty());

        TableColumn<User, String> countryColumn = new TableColumn<>(USERS_TABLE_COLUMN_NAME_COUNTRY);
        countryColumn.setCellValueFactory(param -> param.getValue().getAddress().getCountry().nameProperty());

        usersTable.getColumns().addAll(idColumn, nameColumn, surnameColumn, phoneNumberColumn,
                cityColumn, countryColumn);

        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        usersTable.sortPolicyProperty().set(param -> {
            usersTable.getSelectionModel().clearSelection();
            usersTable.getItems().clear();
            deleteUserButton.setDisable(true);

            FXCollections.sort(usersList, param.getComparator());

            int currentPageIndex = userPagination.getCurrentPageIndex();
            int fromIndex = currentPageIndex * PAGINATION_USERS_PER_PAGE_NUMBER;
            int toIndex = Math.min(fromIndex + PAGINATION_USERS_PER_PAGE_NUMBER, usersList.size());

            usersTable.setItems(FXCollections.observableArrayList(usersList.subList(fromIndex, toIndex)));

            return true;
        });

        usersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) onUserSelected(newValue);
        });

    }

    private Node createUsersPage(int pageIndex) {
        int fromIndex = pageIndex * PAGINATION_USERS_PER_PAGE_NUMBER;
        int toIndex = Math.min(fromIndex + PAGINATION_USERS_PER_PAGE_NUMBER, usersList.size());

        usersTable.setItems(FXCollections.observableArrayList(usersList.subList(fromIndex, toIndex)));

        return new BorderPane(usersTable);
    }

    private int getUsersPagesCount() {
        int pageCount = (int) Math.ceil(usersList.size() / PAGINATION_USERS_PER_PAGE_NUMBER);
        return pageCount > 0 ? pageCount : 1;
    }

    private void setUpUserPagination() {
        userPagination.setPageCount(getUsersPagesCount());
        userPagination.setCurrentPageIndex(PAGINATION_USERS_INIT_PAGE_NUMBER);

        userPagination.setPageFactory(this::createUsersPage);
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

        vehicleLoadTask = generateVehiclesLoadTask(selectedUser.getId());
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
        generateUsersTable();
        setUpUserPagination();

        userCountryComboBox.setItems(CountryDAO.getCountries());

        vehiclePlateNumberColumn.setCellValueFactory(param -> param.getValue().plateNumberProperty());
        vehicleWeightColumn.setCellValueFactory(param -> param.getValue().weightProperty().asObject());
        vehicleHeightColumn.setCellValueFactory(param -> param.getValue().heightProperty().asObject());
        vehicleEngineTypeColumn.setCellValueFactory(param -> param.getValue().getEngine().typeProperty());

        vehicleTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        vehicleTableView.setItems(vehicleList);

        vehicleTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) deleteVehicleButton.setDisable(false);
        });

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
        User editedUser = usersTable.getSelectionModel().getSelectedItem();

        editedUser.setName(userNameInput.getText());
        editedUser.setSurname(userSurnameInput.getText());
        editedUser.setPhoneNumber(userPhoneNumberInput.getText());

            Address editedAddress = editedUser.getAddress();

            editedAddress.setCity(userCityInput.getText());
            editedAddress.setZipCode(userZIPCodeInput.getText());
            editedAddress.setStreet(userStreetInput.getText());
            editedAddress.setNumber(userNumberInput.getText());
            editedAddress.setCountry(userCountryComboBox.getSelectionModel().getSelectedItem());

        editedUser.setAddress(editedAddress);

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
