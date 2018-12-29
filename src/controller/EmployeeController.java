package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.employee.Employee;
import model.employee.EmployeeDAO;
import model.parking.Parking;

public class EmployeeController {

    public TextField parkingLocationInput;
    public TextField addressZipCodeInput;
    public TextField addressStreetInput;
    public TextField addressNumberInput;
    public TextField addressCityInput;
    public TextField addressCountryInput;

    @FXML
    private Button deleteEmployeeButton;

    @FXML
    private TextField parkingIsGuardedInput;
    @FXML
    private TextField parkingIsRoofedInput;
    @FXML
    private TextField parkingStandardLotsNumberInput;
    @FXML
    private TextField parkingDisabledLotsNumberInput;
    @FXML
    private TextField parkingLastControlInput;
    @FXML
    private TextField parkingMaxWeightInput;
    @FXML
    private TextField parkingMaxHeightInput;

    private Task<ObservableList<Employee>> employeeLoadTask;
    @FXML
    private ObservableList<Employee> employeeList;

    @FXML
    public TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> employeeIdColumn;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> surnameColumn;

    @FXML
    private TableColumn<Employee, Float> salaryColumn;

    @FXML
    private TableColumn<Employee, String> departmentColumn;


    public EmployeeController(){
        employeeList = FXCollections.observableArrayList();

        employeeLoadTask = generateEmployeeLoadTask();
    }

    private Task<ObservableList<Employee>> generateEmployeeLoadTask() {
        Task<ObservableList<Employee>> task = new Task<ObservableList<Employee>>() {
            @Override
            protected ObservableList<Employee> call() {
                return EmployeeDAO.getEmployees();
            }
        };

        task.setOnSucceeded(event -> {
            employeeList.clear();
            employeeList.setAll(task.getValue());
        });

        task.setOnFailed(event -> {
            //TODO: show alert
            System.err.println(task.getException().getMessage());
        });

        return task;
    }

    @FXML
    private void initialize() {
        setUpEmployeeTable();

        scheduleLoadTask(employeeLoadTask);
    }

    private void setUpEmployeeTable() {
        employeeIdColumn.setCellValueFactory(param -> param.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(param -> param.getValue().nameProperty());
        surnameColumn.setCellValueFactory(param -> param.getValue().surnameProperty());
        salaryColumn.setCellValueFactory(param -> param.getValue().salaryProperty().asObject());
        departmentColumn.setCellValueFactory(param -> param.getValue().getDepartment().departmentNameProperty());

        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        employeeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) onEmployeeSelected(newValue);
        });

        employeeTable.setItems(employeeList);
    }

    private void onEmployeeSelected(Employee selectedEmployee){

        deleteEmployeeButton.setDisable(false);
        setInputFieldDisable(true);

        addressCityInput.setText(selectedEmployee.getAddress().getCity());
        addressZipCodeInput.setText(selectedEmployee.getAddress().getZipCode());
        addressCountryInput.setText(selectedEmployee.getAddress().getCountry().getName());
        addressStreetInput.setText(selectedEmployee.getAddress().getStreet());
        addressNumberInput.setText(selectedEmployee.getAddress().getNumber());

        parkingStandardLotsNumberInput.setText(Integer.toString(selectedEmployee.getParking().getStandardLots()));
        parkingDisabledLotsNumberInput.setText(Integer.toString(selectedEmployee.getParking().getDisabledLots()));
        parkingIsRoofedInput.setText(Boolean.toString(selectedEmployee.getParking().isRoofed()));
        parkingIsGuardedInput.setText(Boolean.toString(selectedEmployee.getParking().isGuarded()));
        parkingLastControlInput.setText(selectedEmployee.getParking().getLastControl().toString());
        parkingMaxHeightInput.setText(Float.toString(selectedEmployee.getParking().getMaxHeight()));
        parkingMaxWeightInput.setText(Float.toString(selectedEmployee.getParking().getMaxWeight()));
        parkingLocationInput.setText(Float.toString(selectedEmployee.getParking().getLocation().getLatitude()) +
                ", " + Float.toString(selectedEmployee.getParking().getLocation().getLongitude()));

    }

    private void setInputFieldDisable(boolean disable) {

        addressNumberInput.setDisable(disable);
        addressCountryInput.setDisable(disable);
        addressZipCodeInput.setDisable(disable);
        addressCityInput.setDisable(disable);
        addressStreetInput.setDisable(disable);

        parkingStandardLotsNumberInput.setDisable(disable);
        parkingDisabledLotsNumberInput.setDisable(disable);
        parkingIsRoofedInput.setDisable(disable);
        parkingIsGuardedInput.setDisable(disable);
        parkingLastControlInput.setDisable(disable);
        parkingMaxWeightInput.setDisable(disable);
        parkingMaxHeightInput.setDisable(disable);
        parkingLocationInput.setDisable(disable);
    }

    private void scheduleLoadTask(Task task) {
        if(task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }

    public void onDeleteEmployeeButtonClicked(ActionEvent actionEvent) {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        int selectedEmployeeIndex = employeeTable.getSelectionModel().getSelectedIndex();

        if(selectedEmployee != null && selectedEmployeeIndex >= 0) {
            EmployeeDAO.deleteEmployee(selectedEmployee.getId());
            employeeList.remove(selectedEmployeeIndex);
        } else {
            deleteEmployeeButton.setDisable(true);
        }
    }

    public void onRefreshEmployeeButtonClicked(ActionEvent actionEvent) {
        deleteEmployeeButton.setDisable(true);
        scheduleLoadTask(employeeLoadTask);
    }

}
