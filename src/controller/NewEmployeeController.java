package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.address.Address;
import model.address.AddressDAO;
import model.country.Country;
import model.country.CountryDAO;
import model.department.Department;
import model.department.DepartmentDAO;
import model.employee.Employee;
import model.employee.EmployeeDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.Validator;

import java.sql.SQLException;


public class NewEmployeeController {

    private Stage stage;

    @FXML
    private TextField nameInput;
    @FXML
    private TextField surnameInput;
    @FXML
    private TextField salaryInput;
    @FXML
    private ComboBox<Department> departmentComboBox;
    @FXML
    private TextField cityInput;
    @FXML
    private TextField streetInput;
    @FXML
    private TextField zipCodeInput;
    @FXML
    private TextField numberInput;
    private Task<Void> saveEmployeeTask;
    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private ComboBox<Parking> parkingIdComboBox;

    @FXML
    private void initialize() {
        countryComboBox.setItems(CountryDAO.getCountries());
        departmentComboBox.setItems(DepartmentDAO.getDepartment());
        parkingIdComboBox.setItems(ParkingDAO.getNoEmployeeParkings());

        saveEmployeeTask = new Task<Void>() {
            @Override
            protected Void call() throws SQLException {
                
                Parking selectedParking = parkingIdComboBox.getSelectionModel().getSelectedItem();
                Country selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
                Department selectedDepartment = departmentComboBox.getSelectionModel().getSelectedItem();

                Address address = new Address();
                address.setCity(cityInput.getText());
                address.setZipCode(zipCodeInput.getText());
                address.setStreet(streetInput.getText());
                address.setNumber(numberInput.getText());
                address.setCountry(selectedCountry);

                AddressDAO.saveAddress(address);
                Integer addressIndex = AddressDAO.getSavedAddressIndex();

                if(addressIndex != null) {
                    Employee employee = new Employee();

                    employee.setName(nameInput.getText());
                    employee.setSurname(surnameInput.getText());

                    float salary = Float.parseFloat(salaryInput.getText());
                    employee.setSalary(salary);

                    employee.getAddress().setId(addressIndex);
                    employee.getDepartment().setDepartmentName(selectedDepartment.getDepartmentName());
                    employee.getParking().setParkingId(selectedParking.getParkingId());

                    EmployeeDAO.saveEmployee(employee);

                } else {
                    throw new SQLException("Cannot add new employee.");
                }

                return null;
            }
        };

        saveEmployeeTask.setOnFailed(event -> {
            ReportsController.taskAlert(stage, event);
        });
    }

    private void scheduleSaveTask() {
        new Thread(saveEmployeeTask).start();
    }

    private boolean areInputFieldsValid() {
        return Validator.isNameValid(nameInput.getText()) && Validator.isSurnameValid(surnameInput.getText()) &&
                Validator.isSalaryValid(salaryInput.getText()) && Validator.isCityValid(cityInput.getText()) &&
                Validator.isStreetValid(streetInput.getText()) && Validator.isNumberValid(numberInput.getText()) &&
                Validator.isZIPCodeValid(zipCodeInput.getText());
    }

    @FXML
    private void onSaveButtonClicked() {
        if(areInputFieldsValid()) {
            scheduleSaveTask();
            onCancelButtonClicked();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid field(s)");
            alert.show();
        }
    }

    @FXML
    private void onCancelButtonClicked() {
        if(stage != null) stage.close();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}