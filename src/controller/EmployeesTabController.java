package controller;


import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.country.Country;
import model.country.CountryDAO;
import model.department.Department;
import model.department.DepartmentDAO;
import model.views.EmployeesView;
import model.views.EmployeesViewDAO;

import java.sql.Date;
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
    public TableView<EmployeesView> employeesTable;
    @FXML
    private TextField salaryMinInput;
    @FXML
    private TextField salaryMaxInput;
    private List<CheckMenuItem> countryItems;
    private List<CheckMenuItem> departmentItems;
    private List<CheckMenuItem> columnItems;

    private List<TableColumn> columns;

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

        autoShow();

        generateTableColumns();
        setColumns();

    }

    @FXML
    private void autoShow() {


        for(int i = 0; i<countryMenuButton.getItems().size(); i++)
            countryMenuButton.getItems().get(i).setOnAction(e -> e.consume());
        for(int i = 0; i<columnMenuButton.getItems().size(); i++)
            columnMenuButton.getItems().get(i).setOnAction(e -> e.consume());
        for(int i = 0; i<departmentMenuButton.getItems().size(); i++)
            departmentMenuButton.getItems().get(i).setOnAction(e -> e.consume());
    }
    @FXML
    public void onEmployeesFilterClicked()
    {

    }

    private void generateTableColumns()
    {
        columns = new ArrayList<>();
        TableColumn<EmployeesView, Integer> ID = new TableColumn<>("ID");
        ID.prefWidthProperty().bind(employeesTable.widthProperty().divide(19));
        ID.setCellValueFactory(param->param.getValue().idProperty().asObject());
        columns.add(ID);
        TableColumn<EmployeesView, String> name= new TableColumn<>("Name");
        name.prefWidthProperty().bind(employeesTable.widthProperty().divide(19/2));
        name.setCellValueFactory(param->param.getValue().nameProperty());
        columns.add(name);
        TableColumn<EmployeesView, String> surname = new TableColumn<>("Surname");
        surname.prefWidthProperty().bind(employeesTable.widthProperty().divide(19/2));
        surname.setCellValueFactory(param->param.getValue().surnameProperty());
        columns.add(surname);
        TableColumn<EmployeesView, Float> salary = new TableColumn<>("Salary");
        salary.prefWidthProperty().bind(employeesTable.widthProperty().divide(19));
        salary.setCellValueFactory(param->param.getValue().salaryProperty().asObject());
        columns.add(salary);
        TableColumn<EmployeesView, String> country= new TableColumn<>("Country");
        country.prefWidthProperty().bind(employeesTable.widthProperty().divide(19/2));
        country.setCellValueFactory(param->param.getValue().getAddress().getCountry().nameProperty());
        columns.add(country);
        TableColumn<EmployeesView, String> city = new TableColumn<>("City");
        city.prefWidthProperty().bind(employeesTable.widthProperty().divide(19/2));
        city.setCellValueFactory(param->param.getValue().getAddress().cityProperty());
        columns.add(city);
        TableColumn<EmployeesView, String> ZIPcode = new TableColumn<>("ZIP Code");
        ZIPcode.prefWidthProperty().bind(employeesTable.widthProperty().divide(19));
        ZIPcode.setCellValueFactory(param->param.getValue().getAddress().zipCodeProperty());
        columns.add(ZIPcode);
        TableColumn<EmployeesView, String> street = new TableColumn<>("Street");
        street.prefWidthProperty().bind(employeesTable.widthProperty().divide(19/2));
        street.setCellValueFactory(param->param.getValue().getAddress().streetProperty());
        columns.add(street);
        TableColumn<EmployeesView, String> number = new TableColumn<>("Number");
        number.prefWidthProperty().bind(employeesTable.widthProperty().divide(19));
        number.setCellValueFactory(param->param.getValue().getAddress().numberProperty());
        columns.add(number);
        TableColumn<EmployeesView, String> department = new TableColumn<>("Department");
        department.prefWidthProperty().bind(employeesTable.widthProperty().divide(19/2));
        department.setCellValueFactory(param->param.getValue().getDepartment().departmentNameProperty());
        columns.add(department);
        TableColumn<EmployeesView, Integer> parkingID= new TableColumn<>("Parking ID");
        parkingID.prefWidthProperty().bind(employeesTable.widthProperty().divide(19));
        parkingID.setCellValueFactory(param->param.getValue().parking_idProperty().asObject());
        columns.add(parkingID);
        TableColumn<EmployeesView, Date> lastControl = new TableColumn<>("Last Control Date");
        lastControl.prefWidthProperty().bind(employeesTable.widthProperty().divide(19/2));
        lastControl.setCellValueFactory(param->param.getValue().lastControlProperty());
        columns.add(lastControl);
    }
     public void setColumns()
    {
        employeesTable.getColumns().clear();
        if(((CheckMenuItem)columnMenuButton.getItems().get(0)).isSelected())
        {
            for(int i = 0; i<columns.size();i++) {
                employeesTable.getColumns().add(columns.get(i));
            }
            return;
        }
        for(int i = 1; i<columns.size();i++)
        {
            if(((CheckMenuItem)columnMenuButton.getItems().get(i)).isSelected())
                employeesTable.getColumns().add(columns.get(i-1));
        }
        if(employeesTable.getColumns().size()==0)
        {
            for(int i = 0; i<columns.size();i++) {
                employeesTable.getColumns().add(columns.get(i));
            }
            return;
        }
    }


}

