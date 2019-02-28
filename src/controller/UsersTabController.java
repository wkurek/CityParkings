package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.country.CountryDAO;
import model.views.UsersView;
import model.views.UsersViewDAO;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UsersTabController {

    private static final ArrayList<String> COLUMN_NAMES = new ArrayList<>(Arrays.asList(
            "ID",
            "Name",
            "Surname",
            "Phone number",
            "Country",
            "City",
            "ZIP Code",
            "Street",
            "Number",
            "Number of vehicles",
            "Card ID",
            "Card expiration date"));
    @FXML
    public DatePicker expirationMinInput;
    @FXML
    public DatePicker expirationMaxInput;
    @FXML
    public TextField vehiclesMaxInput;
    @FXML
    public TextField vehiclesMinInput;
    @FXML
    public MenuButton countryMenuButton;
    @FXML
    public MenuButton columnMenuButton;
    @FXML
    public TableView<UsersView> usersViewTable;
    @FXML
    public Text nrOfUsers;
    @FXML
    public Text summedVehicles;
    @FXML
    public Text averageVehicles;
    @FXML
    public Text withoutVehicle;
    @FXML
    public Button clearButton;


    private List<CheckMenuItem> countryItems;
    private List<CheckMenuItem> columnItems;
    private List<TableColumn> columns;
    private ObservableList<UsersView> usersViewsList;
    private Task<ObservableList<UsersView>> usersViewTask;
    private Stage stage;

    public UsersTabController() {
        countryItems = new ArrayList<>();
        columnItems = new ArrayList<>();
        columns = new ArrayList<>();
        usersViewsList = FXCollections.observableArrayList();
        usersViewTask = generateUsersViewTask();
    }

    private Task<ObservableList<UsersView>> generateUsersViewTask() {
        Task<ObservableList<UsersView>> task = new Task<>() {
            @Override
            protected ObservableList<UsersView> call() {
                menuButtonsSet();
                String dateMin = null;
                String dateMax = null;
                if (expirationMinInput.getValue() != null) {
                    dateMin = expirationMinInput.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                if (expirationMaxInput.getValue() != null) {
                    dateMax = expirationMaxInput.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                return UsersViewDAO.getUsersViews(vehiclesMinInput.getText(), vehiclesMaxInput.getText(), dateMin, dateMax,
                        ReportsController.selectedMenuItemsToStringList(countryMenuButton.getItems()));
            }
        };

        task.setOnSucceeded(event -> {
            usersViewsList.clear();
            usersViewsList.setAll(task.getValue());
            setUpTable();
            setUpStatistics(usersViewsList);
        });

        task.setOnFailed(event -> ReportsController.taskAlert(stage, event));

        return task;
    }

    @FXML
    private void initialize()
    {
        generateTableColumns();
        setUpTable();
        expirationMinInput.getEditor().setDisable(true);
        expirationMaxInput.getEditor().setDisable(true);
        scheduleLoadTask(usersViewTask);
    }

    private void scheduleLoadTask(Task task) {
        if (task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }
    private void menuButtonsSet()
    {
        columnItems.add(new CheckMenuItem("Select All"));
        COLUMN_NAMES.forEach(e->columnItems.add(new CheckMenuItem(e)));
        columnMenuButton.getItems().setAll(columnItems);

        countryItems.add(new CheckMenuItem("Select All"));
        CountryDAO.getCountries().forEach(e->countryItems.add(new CheckMenuItem(e.getName())));
        countryMenuButton.getItems().setAll(countryItems);
    }
    private void generateTableColumns()
    {
        TableColumn<UsersView, Integer> ID = new TableColumn<>(COLUMN_NAMES.get(0));
        ID.setCellValueFactory(param->param.getValue().idProperty().asObject());
        columns.add(ID);
        TableColumn<UsersView, String> name= new TableColumn<>(COLUMN_NAMES.get(1));
        name.setCellValueFactory(param->param.getValue().nameProperty());
        columns.add(name);
        TableColumn<UsersView, String> surname = new TableColumn<>(COLUMN_NAMES.get(2));
        surname.setCellValueFactory(param->param.getValue().surnameProperty());
        columns.add(surname);
        TableColumn<UsersView, String> phoneNumber = new TableColumn<>(COLUMN_NAMES.get(3));
        phoneNumber.setCellValueFactory(param->param.getValue().phoneNumberProperty());
        columns.add(phoneNumber);
        TableColumn<UsersView, String> country = new TableColumn<>(COLUMN_NAMES.get(4));
        country.setCellValueFactory(param->param.getValue().countryProperty());
        columns.add(country);
        TableColumn<UsersView, String> city = new TableColumn<>(COLUMN_NAMES.get(5));
        city.setCellValueFactory(param->param.getValue().cityProperty());
        columns.add(city);
        TableColumn<UsersView, String> ZIPcode = new TableColumn<>(COLUMN_NAMES.get(6));
        ZIPcode.setCellValueFactory(param->param.getValue().zip_codeProperty());
        columns.add(ZIPcode);
        TableColumn<UsersView, String> street = new TableColumn<>(COLUMN_NAMES.get(7));
        street.setCellValueFactory(param->param.getValue().streetProperty());
        columns.add(street);
        TableColumn<UsersView, String> number = new TableColumn<>(COLUMN_NAMES.get(8));
        number.setCellValueFactory(param->param.getValue().numberProperty());
        columns.add(number);
        TableColumn<UsersView, Integer> nrOfVehicles = new TableColumn<>(COLUMN_NAMES.get(9));
        nrOfVehicles.setCellValueFactory(param->param.getValue().numberOfVehiclesProperty().asObject());
        columns.add(nrOfVehicles);
        TableColumn<UsersView, Integer> cardID= new TableColumn<>(COLUMN_NAMES.get(10));
        cardID.setCellValueFactory(param->param.getValue().cardIDProperty().asObject());
        columns.add(cardID);
        TableColumn<UsersView, Date> expirationDate = new TableColumn<>(COLUMN_NAMES.get(11));
        expirationDate.setCellValueFactory(param->param.getValue().expirationDateProperty());
        columns.add(expirationDate);
    }
    private void setUpTable()
    {
        ReportsController.setColumns(usersViewTable, columns, columnMenuButton);
        usersViewTable.setItems(usersViewsList);
    }
    @FXML
    public void onUsersFilterClicked() {
        usersViewTask = generateUsersViewTask();
        scheduleLoadTask(usersViewTask);
    }

    private void setUpStatistics(ObservableList<UsersView> usersViewsList) {
        UsersViewDAO.generateStatistics(usersViewsList);
        nrOfUsers.setText(Integer.toString(UsersViewDAO.getNrOfUsers()));
        summedVehicles.setText(Integer.toString(UsersViewDAO.getSummedVehicles()));
        averageVehicles.setText(String.format("%.02f", UsersViewDAO.getAverageVehicles()));
        withoutVehicle.setText(Integer.toString(UsersViewDAO.getWithoutVehicle()));
    }


    public void onClearButtonClicked() {
        expirationMinInput.setValue(null);
        expirationMaxInput.setValue(null);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
