package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.address.AddressDAO;
import model.country.Country;
import model.country.CountryDAO;
import model.department.Department;
import model.department.DepartmentDAO;
import model.employee.Employee;
import model.employee.EmployeeDAO;
import util.Validator;

import java.io.IOException;

public class EmployeeController {

    public TextField employeeNameInput;
    public TextField employeeSurnameInput;
    public ComboBox<Country> addressCountryComboBox;
    @FXML
    private ComboBox<Department> employeeDepartmentComboBox;
    public TextField employeeSalaryInput;
    public Button editEmployeeButton;
    public Button saveEmployeeButton;
    private Stage stage;

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

    @FXML
    private TableColumn<Employee, String> countryColumn;


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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("SQL Error");
            alert.setHeaderText(event.getSource().getException().getMessage());
            alert.show();
        });

        return task;
    }

    @FXML
    private void initialize() {
        setUpEmployeeTable();

        employeeDepartmentComboBox.setItems(DepartmentDAO.getDepartment());
        addressCountryComboBox.setItems(CountryDAO.getCountries());
        scheduleLoadTask(employeeLoadTask);
    }

    private void setUpEmployeeTable() {
        employeeIdColumn.setCellValueFactory(param -> param.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(param -> param.getValue().nameProperty());
        surnameColumn.setCellValueFactory(param -> param.getValue().surnameProperty());
        salaryColumn.setCellValueFactory(param -> param.getValue().salaryProperty().asObject());
        departmentColumn.setCellValueFactory(param -> param.getValue().getDepartment().departmentNameProperty());
        countryColumn.setCellValueFactory(param -> param.getValue().getAddress().getCountry().nameProperty());

        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        employeeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) onEmployeeSelected(newValue);
        });

        employeeTable.setItems(employeeList);
    }

    private void onEmployeeSelected(Employee selectedEmployee){

        deleteEmployeeButton.setDisable(false);
        saveEmployeeButton.setDisable(true);
        editEmployeeButton.setDisable(false);
        setEmployeeInputFieldDisable(true);


        employeeNameInput.setText(selectedEmployee.getName());
        employeeSurnameInput.setText(selectedEmployee.getSurname());
        employeeSalaryInput.setText(Float.toString(selectedEmployee.getSalary()));

        addressCityInput.setText(selectedEmployee.getAddress().getCity());
        addressZipCodeInput.setText(selectedEmployee.getAddress().getZipCode());
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

    private void setEmployeeInputFieldDisable(boolean disable) {
        employeeNameInput.setDisable(disable);
        employeeSurnameInput.setDisable(disable);
        employeeSalaryInput.setDisable(disable);
        addressCityInput.setDisable(disable);
        addressZipCodeInput.setDisable(disable);
        addressStreetInput.setDisable(disable);
        addressNumberInput.setDisable(disable);

        employeeDepartmentComboBox.setDisable(disable);
        addressCountryComboBox.setDisable(disable);
    }

    private void scheduleLoadTask(Task task) {
        if(task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }

    @FXML
    public void onNewEmployeeButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EmployeeController.class.getResource("../view/newEmployeeView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Employee");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            NewEmployeeController controller = loader.getController();
            controller.setStage(dialogStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        employeeLoadTask = generateEmployeeLoadTask();
        scheduleLoadTask(employeeLoadTask);
    }

    @FXML
    public void onEditEmployeeButtonClicked() {
        editEmployeeButton.setDisable(true);
        saveEmployeeButton.setDisable(false);

        setEmployeeInputFieldDisable(false);
    }

    private boolean areEditedFieldsValid() {
        return Validator.isNameValid(employeeNameInput.getText()) && Validator.isSurnameValid(employeeSurnameInput.getText()) &&
                Validator.isSalaryValid(employeeSalaryInput.getText()) &&
                Validator.isCityValid(addressCityInput.getText()) && Validator.isStreetValid(addressStreetInput.getText()) &&
                Validator.isNumberValid(addressNumberInput.getText()) && Validator.isZIPCodeValid(addressZipCodeInput.getText());
    }

    @FXML
    public void onSaveEmployeeButtonClicked() {
        if(areEditedFieldsValid()) {
            int editedEmployeeIndex = employeeTable.getSelectionModel().getSelectedIndex();
            Employee editedEmployee = employeeList.get(editedEmployeeIndex);

            editedEmployee.setName(employeeNameInput.getText());
            editedEmployee.setSurname(employeeSurnameInput.getText());
            editedEmployee.setSalary(Float.valueOf(employeeSalaryInput.getText()));

            editedEmployee.getAddress().setCity(addressCityInput.getText());
            editedEmployee.getAddress().setZipCode(addressZipCodeInput.getText());
            editedEmployee.getAddress().setStreet(addressStreetInput.getText());
            editedEmployee.getAddress().setNumber(addressNumberInput.getText());

            Department selectedDepartment = employeeDepartmentComboBox.getSelectionModel().getSelectedItem();
            editedEmployee.getDepartment().setDepartmentName(selectedDepartment.getDepartmentName());

            Country selectedCountry = addressCountryComboBox.getSelectionModel().getSelectedItem();
            editedEmployee.getAddress().getCountry().setName(selectedCountry.getName());
            editedEmployee.getAddress().getCountry().setIso(selectedCountry.getIso());

            AddressDAO.updateAddress(editedEmployee.getAddress().getId(), editedEmployee.getAddress());
            EmployeeDAO.updateEmployee(editedEmployee.getId(), editedEmployee);

            editEmployeeButton.setDisable(false);
            saveEmployeeButton.setDisable(true);
            setEmployeeInputFieldDisable(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid field(s)");
            alert.show();
        }
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
