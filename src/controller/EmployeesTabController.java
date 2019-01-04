package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import model.country.Country;
import model.country.CountryDAO;
import model.department.Department;
import model.department.DepartmentDAO;
import model.views.EmployeesViewDAO;

import java.util.ArrayList;
import java.util.List;

public class EmployeesTabController {

    @FXML
    public MenuButton countryMenuButton;
    @FXML
    public MenuButton departmentMenuButton;
    @FXML
    public MenuButton columnMenuButton;
    @FXML
    private TextField salaryMinInput;
    @FXML
    private TextField salaryMaxInput;
    private List<CheckMenuItem> countryItems;
    private List<CheckMenuItem> departmentItems;
    private List<CheckMenuItem> columnItems;


    public EmployeesTabController()
    {
        countryItems = new ArrayList<>();
        departmentItems = new ArrayList<>();
        columnItems = new ArrayList<>();
    }

    @FXML
    private void initialize()
    {
        countryItems.add(new CheckMenuItem("Select All"));
        for(Country i : CountryDAO.getCountries()) {
            countryItems.add(new CheckMenuItem(i.toString()));
        }
        countryMenuButton.getItems().setAll(countryItems);

        departmentItems.add(new CheckMenuItem("Select All"));
        for(Department i : DepartmentDAO.getDepartment()) {
            departmentItems.add(new CheckMenuItem(i.getDepartmentName()));
        }
        departmentMenuButton.getItems().setAll(departmentItems);

        columnItems.add(new CheckMenuItem("Select All"));
        for(String i : EmployeesViewDAO.getColumnsNames()) {
            columnItems.add(new CheckMenuItem(i));
        }
        columnMenuButton.getItems().setAll(columnItems);



    }
    @FXML
    public void onEmployeesFilterClicked()
    {

    }

}

