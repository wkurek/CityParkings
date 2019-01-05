package controller;


import javafx.event.EventHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.country.Country;
import model.country.CountryDAO;
import model.department.Department;
import model.department.DepartmentDAO;
import model.views.EmployeesView;
import model.views.EmployeesViewDAO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeesTabController {


    private static final ArrayList<String> COLUMN_NAMES = new ArrayList<>(Arrays.asList(
            "ID",
            "Name",
            "Surname",
            "Salary",
            "Country",
            "City",
            "ZIP Code",
            "Street",
            "Number",
            "Department Name",
            "Parking ID",
            "Parking's last control"));
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
    private Stage stage;

    private ObservableList<EmployeesView> employeesViewList;
    private Task<ObservableList<EmployeesView>> employeesViewLoadTask;

    public EmployeesTabController()
    {
        countryItems = new ArrayList<>();
        departmentItems = new ArrayList<>();
        columnItems = new ArrayList<>();
        employeesViewList = FXCollections.observableArrayList();

        employeesViewLoadTask = generateEmployeesViewsLoadTask();
    }

    @FXML
    private void initialize()
    {
        menuButtonsSet();
        autoShowOff();
        generateTableColumns();
        setColumns();
        employeesTable.setItems(employeesViewList);
        scheduleLoadTask(employeesViewLoadTask);
    }

    private void menuButtonsSet() {
        countryItems.add(new CheckMenuItem("Select All"));
        for(Country i : CountryDAO.getCountries()) {
            countryItems.add(new CheckMenuItem(i.getName()));
        }
        countryMenuButton.getItems().setAll(countryItems);

        departmentItems.add(new CheckMenuItem("Select All"));
        for(Department i : DepartmentDAO.getDepartment()) {
            departmentItems.add(new CheckMenuItem(i.getDepartmentName()));
        }
        departmentMenuButton.getItems().setAll(departmentItems);

        columnItems.add(new CheckMenuItem("Select All"));
        for(String i : COLUMN_NAMES) {
            columnItems.add(new CheckMenuItem(i));
        }
        columnMenuButton.getItems().setAll(columnItems);
    }

    @FXML
    private void autoShowOff() {


        for(int i = 0; i<countryMenuButton.getItems().size(); i++)
            countryMenuButton.getItems().get(i).setOnAction(e -> e.consume());
        for(int i = 0; i<columnMenuButton.getItems().size(); i++)
            columnMenuButton.getItems().get(i).setOnAction(e -> e.consume());
        for(int i = 0; i<departmentMenuButton.getItems().size(); i++)
            departmentMenuButton.getItems().get(i).setOnAction(e -> e.consume());
    }

    private Task<ObservableList<EmployeesView>> generateEmployeesViewsLoadTask() {
        Task<ObservableList<EmployeesView>> task = new Task<>() {
            @Override
            protected ObservableList<EmployeesView> call() {
                return EmployeesViewDAO.getEmployeesViews(salaryMinInput.getText(), salaryMaxInput.getText(),
                                                        selectedMenuItemsToStringList(countryMenuButton.getItems()),
                                                        selectedMenuItemsToStringList(departmentMenuButton.getItems()));
            }
        };

        task.setOnSucceeded(event -> {
            employeesViewList.clear();
            employeesViewList.addAll(task.getValue());
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
    private void scheduleLoadTask(Task task) {
        if(task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }
    private void generateTableColumns()
    {
        columns = new ArrayList<>();
        TableColumn<EmployeesView, Integer> ID = new TableColumn<>("ID");
        ID.setCellValueFactory(param->param.getValue().idProperty().asObject());
        columns.add(ID);
        TableColumn<EmployeesView, String> name= new TableColumn<>("Name");
        name.setCellValueFactory(param->param.getValue().nameProperty());
        columns.add(name);
        TableColumn<EmployeesView, String> surname = new TableColumn<>("Surname");
        surname.setCellValueFactory(param->param.getValue().surnameProperty());
        columns.add(surname);
        TableColumn<EmployeesView, Float> salary = new TableColumn<>("Salary");
        salary.setCellValueFactory(param->param.getValue().salaryProperty().asObject());
        columns.add(salary);
        TableColumn<EmployeesView, String> country = new TableColumn<>("Country");
        country.setCellValueFactory(param->param.getValue().countryProperty());
        columns.add(country);
        TableColumn<EmployeesView, String> city = new TableColumn<>("City");
        city.setCellValueFactory(param->param.getValue().cityProperty());
        columns.add(city);
        TableColumn<EmployeesView, String> ZIPcode = new TableColumn<>("ZIP Code");
        ZIPcode.setCellValueFactory(param->param.getValue().zip_codeProperty());
        columns.add(ZIPcode);
        TableColumn<EmployeesView, String> street = new TableColumn<>("Street");
        street.setCellValueFactory(param->param.getValue().streetProperty());
        columns.add(street);
        TableColumn<EmployeesView, String> number = new TableColumn<>("Number");
        number.setCellValueFactory(param->param.getValue().numberProperty());
        columns.add(number);
        TableColumn<EmployeesView, String> department = new TableColumn<>("Department");
        department.setCellValueFactory(param->param.getValue().getDepartment().departmentNameProperty());
        columns.add(department);
        TableColumn<EmployeesView, Integer> parkingID= new TableColumn<>("Parking ID");
        parkingID.setCellValueFactory(param->param.getValue().parking_idProperty().asObject());
        columns.add(parkingID);
        TableColumn<EmployeesView, Date> lastControl = new TableColumn<>("Last Control Date");
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
            employeesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            return;
        }
        for(int i = 1; i<columns.size();i++)
        {
            if(((CheckMenuItem)columnMenuButton.getItems().get(i)).isSelected())
                employeesTable.getColumns().add(columns.get(i-1));
            employeesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        if(employeesTable.getColumns().size()==0)
        {
            for(int i = 0; i<columns.size();i++) {
                employeesTable.getColumns().add(columns.get(i));
            }
            employeesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            return;
        }
    }

    private List<String> selectedMenuItemsToStringList(List<MenuItem> items)
    {
        List<String> result = new ArrayList<>();
        for(MenuItem m : items)
        {
            CheckMenuItem item = (CheckMenuItem)m;
            if(item.isSelected())
               result.add(m.getText());
        }
        return result;
    }
    @FXML
    public void onFilterButtonClicked() {
        setColumns();
        employeesViewList=EmployeesViewDAO.getEmployeesViews(salaryMinInput.getText(), salaryMaxInput.getText(),
                  selectedMenuItemsToStringList(countryMenuButton.getItems()),
                  selectedMenuItemsToStringList(departmentMenuButton.getItems()));
        employeesTable.setItems(employeesViewList);
    }
}

