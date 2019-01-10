package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import model.country.Country;
import model.country.CountryDAO;
import model.engine.Engine;
import model.engine.EngineDAO;
import model.views.ParkingsView;
import model.views.VehiclesView;
import model.views.VehiclesViewDAO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VehiclesTabController {

    private static final ArrayList<String> COLUMN_NAMES = new ArrayList<>(Arrays.asList(
            "ID",
            "Plate number",
            "Height",
            "Weight",
            "Engine Type",
            "Owner name",
            "Owner surname",
            "Owner phone nr",
            "Owner country",
            "Owner city",
            "Owner city ZIP code",
            "Owner street",
            "Owner home nr",
            "Parked on (parking id)",
            "Parked at (time)"));
    @FXML
    public TextField weightMinInput;
    @FXML
    public TextField weightMaxInput;
    @FXML
    public CheckBox parkedCheckBox;
    @FXML
    public MenuButton engineTypeMenuButton;
    @FXML
    public MenuButton countryMenuButton;
    @FXML
    public MenuButton columnMenuButton;
    @FXML
    public TextField heightMinInput;
    @FXML
    public TextField heightMaxInput;
    @FXML
    public TableView<VehiclesView> vehiclesViewTable;
    @FXML
    public Text nrOfVehicles;
    @FXML
    public Text nrOfVehiclesParked;
    @FXML
    public Text onCityParkings;
    @FXML
    public Text onParkRides;
    @FXML
    public Text onKissRides;
    @FXML
    public Text onEstateParkings;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Text nrOfVehiclesLabel;

    private List<CheckMenuItem> engineTypeItems;
    private List<CheckMenuItem> countryItems;
    private List<CheckMenuItem> columnItems;

    private ObservableList<VehiclesView> vehiclesViewsList;
    private List<TableColumn> columns;
    public VehiclesTabController()
    {
        countryItems = new ArrayList<>();
        engineTypeItems = new ArrayList<>();
        columnItems = new ArrayList<>();
        columns = new ArrayList<>();

        vehiclesViewsList = FXCollections.observableArrayList();


    }
    @FXML
    private void initialize()
    {
        menuButtonsSet();
        generateTableColumns();
        setUpTable();
        setUpStatistics();
    }



    private void setUpTable() {
        ReportsController.setColumns(vehiclesViewTable, columns, columnMenuButton);
        vehiclesViewsList = VehiclesViewDAO.getVehiclesViews(heightMinInput.getText(), heightMaxInput.getText(), weightMinInput.getText(), weightMaxInput.getText(),
                                                               ReportsController.selectedMenuItemsToStringList(countryMenuButton.getItems()),
                                                                ReportsController.selectedMenuItemsToStringList(engineTypeMenuButton.getItems()),
                                                                parkedCheckBox.isSelected());
        vehiclesViewTable.setItems(vehiclesViewsList);
    }

    private void menuButtonsSet()
    {
        countryItems.add(new CheckMenuItem("Select All"));
        for(Country i : CountryDAO.getCountries()) {
            countryItems.add(new CheckMenuItem(i.getName()));
        }
        countryMenuButton.getItems().setAll(countryItems);

        engineTypeItems.add(new CheckMenuItem("Select All"));
        for(Engine i : EngineDAO.getEngines()) {
            engineTypeItems.add(new CheckMenuItem(i.getType()));
        }
        engineTypeMenuButton.getItems().setAll(engineTypeItems);

        columnItems.add(new CheckMenuItem("Select All"));
        for(String i : COLUMN_NAMES) {
            columnItems.add(new CheckMenuItem(i));
        }
        columnMenuButton.getItems().setAll(columnItems);
    }
    @FXML
    public void onVehiclesFilterClicked() {
        setUpTable();
        setUpStatistics();
    }
    private void generateTableColumns()
    {
        TableColumn<VehiclesView, Integer> ID = new TableColumn<>(COLUMN_NAMES.get(0));
        ID.setCellValueFactory(param->param.getValue().idProperty().asObject());
        columns.add(ID);
        TableColumn<VehiclesView, String> plateNumber= new TableColumn<>(COLUMN_NAMES.get(1));
        plateNumber.setCellValueFactory(param->param.getValue().plateNumberProperty());
        columns.add(plateNumber);
        TableColumn<VehiclesView, Float> height = new TableColumn<>(COLUMN_NAMES.get(2));
        height.setCellValueFactory(param->param.getValue().heightProperty().asObject());
        columns.add(height);
        TableColumn<VehiclesView, Float> weight = new TableColumn<>(COLUMN_NAMES.get(3));
        weight.setCellValueFactory(param->param.getValue().weightProperty().asObject());
        columns.add(weight);
        TableColumn<VehiclesView, String> engineType = new TableColumn<>(COLUMN_NAMES.get(4));
        engineType.setCellValueFactory(param->param.getValue().engineTypeProperty());
        columns.add(engineType);
        TableColumn<VehiclesView, String> ownerName = new TableColumn<>(COLUMN_NAMES.get(5));
        ownerName.setCellValueFactory(param->param.getValue().ownerNameProperty());
        columns.add(ownerName);
        TableColumn<VehiclesView, String> ownerSurname = new TableColumn<>(COLUMN_NAMES.get(6));
        ownerSurname.setCellValueFactory(param->param.getValue().ownerSurnameProperty());
        columns.add(ownerSurname);
        TableColumn<VehiclesView, String> phoneNumber = new TableColumn<>(COLUMN_NAMES.get(7));
        phoneNumber.setCellValueFactory(param->param.getValue().phoneNumberProperty());
        columns.add(phoneNumber);
        TableColumn<VehiclesView, String> country = new TableColumn<>(COLUMN_NAMES.get(8));
        country.setCellValueFactory(param->param.getValue().countryProperty());
        columns.add(country);
        TableColumn<VehiclesView, String> city = new TableColumn<>(COLUMN_NAMES.get(9));
        city.setCellValueFactory(param->param.getValue().cityProperty());
        columns.add(city);
        TableColumn<VehiclesView, String> ZIPcode = new TableColumn<>(COLUMN_NAMES.get(10));
        ZIPcode.setCellValueFactory(param->param.getValue().zip_codeProperty());
        columns.add(ZIPcode);
        TableColumn<VehiclesView, String> street = new TableColumn<>(COLUMN_NAMES.get(11));
        street.setCellValueFactory(param->param.getValue().streetProperty());
        columns.add(street);
        TableColumn<VehiclesView, String> number = new TableColumn<>(COLUMN_NAMES.get(12));
        number.setCellValueFactory(param->param.getValue().numberProperty());
        columns.add(number);
        TableColumn<VehiclesView, Integer> parkingID= new TableColumn<>(COLUMN_NAMES.get(13));
        ReportsController.setIntegerColumnsNullable(parkingID);
        parkingID.setCellValueFactory(param->param.getValue().parkingIDProperty());
        columns.add(parkingID);
        TableColumn<VehiclesView, Date> parkDateTime = new TableColumn<>(COLUMN_NAMES.get(14));
        parkDateTime.setCellValueFactory(param->param.getValue().parkDateTimeProperty());
        columns.add(parkDateTime);
    }

    private void setUpStatistics() {
        VehiclesViewDAO.generateStatistics(heightMinInput.getText(), heightMaxInput.getText(), weightMinInput.getText(), weightMaxInput.getText(),
                ReportsController.selectedMenuItemsToStringList(countryMenuButton.getItems()),
                ReportsController.selectedMenuItemsToStringList(engineTypeMenuButton.getItems()),
                parkedCheckBox.isSelected());
        nrOfVehicles.setText(Integer.toString(VehiclesViewDAO.getNrOfVehicles()));
        nrOfVehiclesParked.setText(Integer.toString(VehiclesViewDAO.getNrOfVehiclesParked()));
        onCityParkings.setText(Integer.toString(VehiclesViewDAO.getOnCityParkings()));
        onParkRides.setText(Integer.toString(VehiclesViewDAO.getOnParkRides()));
        onKissRides.setText(Integer.toString(VehiclesViewDAO.getOnKissRides()));
        onEstateParkings.setText(Integer.toString(VehiclesViewDAO.getOnEstateParkings()));
        Map<String, Integer> engineType = VehiclesViewDAO.getEngineTypesAndNumber();
        double x_position, y_position, number_x_position;
        x_position = nrOfVehiclesLabel.getLayoutX();
        y_position = 0.0;
        number_x_position = nrOfVehicles.getLayoutX();
        anchorPane.getChildren().clear();
        for(Map.Entry<String, Integer> entry : engineType.entrySet())
        {
            System.out.println(entry.getKey()+": "+entry.getValue());
            y_position+=21.0;
            Text engineLabel = new Text(entry.getKey()+": ");
            engineLabel.setLayoutX(x_position);
            engineLabel.setLayoutY(y_position);
            anchorPane.getChildren().add(engineLabel);
            Text engineNumberLabel = new Text(Integer.toString(entry.getValue()));
            engineNumberLabel.setLayoutX(number_x_position);
            engineNumberLabel.setLayoutY(y_position);
            anchorPane.getChildren().add(engineNumberLabel);
        }
    }

}
