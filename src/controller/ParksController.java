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
import model.user.User;
import model.vehicle.Vehicle;

public class ParksController {

    private static final int PAGINATION_PARKS_PER_PAGE_NUMBER = 8;
    private static final int PAGINATION_PARKS_INIT_PAGE_NUMBER = 0;

    private static final String PARKS_TABLE_COLUMN_NAME_DATE= "Park date";
    private static final String PARKS_TABLE_COLUMN_NAME_VEHICLE_ID= "Vehicle ID";
    private static final String PARKS_TABLE_COLUMN_NAME_PARKING_ID= "Parking ID";

    private ObservableList<User> usersList;

    private Task<ObservableList<User>> usersLoadTask;

    private TableView<User> usersTable;

    public ParksController() {
        usersList = FXCollections.observableArrayList();
        vehicleList = FXCollections.observableArrayList();

        usersLoadTask = generateUsersLoadTask();
    }


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
