package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.user.User;
import model.user.UserDAO;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;

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
    private Button refreshUserButton;
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

    public UserController() {
        usersList = FXCollections.observableArrayList();
        vehicleList = FXCollections.observableArrayList();

        usersLoadTask = generateUsersLoadTask();
    }

    private Task<ObservableList<Vehicle>> generateVehiclesLoadTask(final int userId) {
        Task<ObservableList<Vehicle>> task = new Task<ObservableList<Vehicle>>() {
            @Override
            protected ObservableList<Vehicle> call() throws Exception {
                return VehicleDAO.getVehicles();
            }
        };

        task.setOnSucceeded(event -> {
            vehicleList = task.getValue();
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
            protected ObservableList<User> call() throws Exception {
                return UserDAO.getUsers();
            }
        };

        task.setOnSucceeded(event -> {
            usersList = task.getValue();
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

        usersTable.getColumns().addAll(idColumn, nameColumn, surnameColumn, phoneNumberColumn, cityColumn, countryColumn);

        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        usersTable.sortPolicyProperty().set(param -> {
            FXCollections.sort(usersList, param.getComparator());

            int currentPageIndex = userPagination.getCurrentPageIndex();
            int fromIndex = currentPageIndex * PAGINATION_USERS_PER_PAGE_NUMBER;
            int toIndex = Math.min(fromIndex + PAGINATION_USERS_PER_PAGE_NUMBER, usersList.size());

            usersTable.setItems(FXCollections.observableArrayList(usersList.subList(fromIndex, toIndex)));

            return true;
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

    @FXML
    private void initialize () {
        generateUsersTable();
        setUpUserPagination();

        scheduleLoadTask(usersLoadTask);
    }

    @FXML
    public void onRefreshUserButtonClicked(ActionEvent actionEvent) {
        scheduleLoadTask(usersLoadTask);
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
