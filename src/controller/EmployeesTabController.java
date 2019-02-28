package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.country.CountryDAO;
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
            "Department",
            "Parking ID",
            "Parking's Last Control Date"));

    @FXML
    public MenuButton countryMenuButton;
    @FXML
    public MenuButton departmentMenuButton;
    @FXML
    public MenuButton columnMenuButton;
    @FXML
    public TableView<EmployeesView> employeesViewTable;
    @FXML
    public Text minSalary;
    @FXML
    public Text medianSalary;
    @FXML
    public Text averageSalary;
    @FXML
    public Text maxSalary;
    @FXML
    public Text nrOfEmployees;
    @FXML
    public Text nrOfParkingsOp;
    @FXML
    public Text nrOfDepartments;
    @FXML
    public Text minEmployees;
    @FXML
    public Text maxEmployees;
    @FXML
    public Text averageEmployees;
    @FXML
    public Text minDepartment;
    @FXML
    public Text maxDepartment;
    @FXML
    private TextField salaryMinInput;
    @FXML
    private TextField salaryMaxInput;
    private Stage stage;
    private List<CheckMenuItem> countryItems;
    private List<CheckMenuItem> departmentItems;
    private List<CheckMenuItem> columnItems;

    private List<TableColumn> columns;

    private ObservableList<EmployeesView> employeesViewList;

    private Task<ObservableList<EmployeesView>> employeesViewTask;

    public EmployeesTabController()
    {
        columns = new ArrayList<>();
        countryItems = new ArrayList<>();
        departmentItems = new ArrayList<>();
        columnItems = new ArrayList<>();
        employeesViewList = FXCollections.observableArrayList();
        employeesViewTask = generateEmployeesViewTask();
    }

    private Task<ObservableList<EmployeesView>> generateEmployeesViewTask() {
        Task<ObservableList<EmployeesView>> task = new Task<>() {
            @Override
            protected ObservableList<EmployeesView> call() {
                menuButtonsSet();
                return EmployeesViewDAO.getEmployeesViews(salaryMinInput.getText(), salaryMaxInput.getText(),
                        ReportsController.selectedMenuItemsToStringList(countryMenuButton.getItems()),
                        ReportsController.selectedMenuItemsToStringList(departmentMenuButton.getItems()));
            }
        };

        task.setOnSucceeded(event -> {
            employeesViewList.clear();
            employeesViewList.setAll(task.getValue());
            setUpTable();
            setUpStatistics(employeesViewList);
        });

        task.setOnFailed(event -> ReportsController.taskAlert(stage, event));

        return task;
    }
    @FXML
    private void initialize()
    {
         // autoShowOff();
          generateTableColumns();
          setUpTable();
        scheduleLoadTask(employeesViewTask);
    }

    private void scheduleLoadTask(Task task) {
        if (task != null && task.isRunning()) task.cancel();

        new Thread(task).start();
    }
    @FXML
    private void menuButtonsSet() {

        countryItems.add(new CheckMenuItem("Select All"));
        CountryDAO.getCountries().forEach(e->countryItems.add(new CheckMenuItem(e.getName())));
        countryMenuButton.getItems().setAll(countryItems);


        departmentItems.add(new CheckMenuItem("Select All"));
        DepartmentDAO.getDepartment().forEach(e->departmentItems.add(new CheckMenuItem(e.getDepartmentName())));
        departmentMenuButton.getItems().setAll(departmentItems);

        columnItems.add(new CheckMenuItem("Select All"));
        COLUMN_NAMES.forEach(e->columnItems.add(new CheckMenuItem(e)));
        columnMenuButton.getItems().setAll(columnItems);
    }

    @FXML
    private void autoShowOff() {
        for(int i = 0; i<countryMenuButton.getItems().size(); i++)
            countryMenuButton.getItems().get(i).setOnAction(Event::consume);
        for(int i = 0; i<columnMenuButton.getItems().size(); i++)
            columnMenuButton.getItems().get(i).setOnAction(Event::consume);
        for(int i = 0; i<departmentMenuButton.getItems().size(); i++)
            departmentMenuButton.getItems().get(i).setOnAction(Event::consume);
    }
    private void generateTableColumns()
    {
        TableColumn<EmployeesView, Integer> ID = new TableColumn<>(COLUMN_NAMES.get(0));
        ID.setCellValueFactory(param->param.getValue().idProperty().asObject());
        columns.add(ID);
        TableColumn<EmployeesView, String> name= new TableColumn<>(COLUMN_NAMES.get(1));
        name.setCellValueFactory(param->param.getValue().nameProperty());
        columns.add(name);
        TableColumn<EmployeesView, String> surname = new TableColumn<>(COLUMN_NAMES.get(2));
        surname.setCellValueFactory(param->param.getValue().surnameProperty());
        columns.add(surname);
        TableColumn<EmployeesView, Float> salary = new TableColumn<>(COLUMN_NAMES.get(3));
        salary.setCellValueFactory(param->param.getValue().salaryProperty().asObject());
        columns.add(salary);
        TableColumn<EmployeesView, String> country = new TableColumn<>(COLUMN_NAMES.get(4));
        country.setCellValueFactory(param->param.getValue().countryProperty());
        columns.add(country);
        TableColumn<EmployeesView, String> city = new TableColumn<>(COLUMN_NAMES.get(5));
        city.setCellValueFactory(param->param.getValue().cityProperty());
        columns.add(city);
        TableColumn<EmployeesView, String> ZIPcode = new TableColumn<>(COLUMN_NAMES.get(6));
        ZIPcode.setCellValueFactory(param->param.getValue().zip_codeProperty());
        columns.add(ZIPcode);
        TableColumn<EmployeesView, String> street = new TableColumn<>(COLUMN_NAMES.get(7));
        street.setCellValueFactory(param->param.getValue().streetProperty());
        columns.add(street);
        TableColumn<EmployeesView, String> number = new TableColumn<>(COLUMN_NAMES.get(8));
        number.setCellValueFactory(param->param.getValue().numberProperty());
        columns.add(number);
        TableColumn<EmployeesView, String> department = new TableColumn<>(COLUMN_NAMES.get(9));
        department.setCellValueFactory(param->param.getValue().getDepartment().departmentNameProperty());
        columns.add(department);
        TableColumn<EmployeesView, Integer> parkingID= new TableColumn<>(COLUMN_NAMES.get(10));
        ReportsController.setIntegerColumnsNullable(parkingID);
        parkingID.setCellValueFactory(param->param.getValue().parkingIDProperty().asObject());
        columns.add(parkingID);
        TableColumn<EmployeesView, Date> lastControl = new TableColumn<>(COLUMN_NAMES.get(11));
        lastControl.setCellValueFactory(param->param.getValue().lastControlProperty());
        columns.add(lastControl);
    }
    @FXML
    public void onFilterButtonClicked() {
        employeesViewTask = generateEmployeesViewTask();
        scheduleLoadTask(employeesViewTask);
    }
    private void setUpTable()
    {
        ReportsController.setColumns(employeesViewTable, columns, columnMenuButton);
        employeesViewTable.setItems(employeesViewList);
    }


    private void setUpStatistics(ObservableList<EmployeesView> employeesViewList)
    {
        EmployeesViewDAO.generateStatistics(employeesViewList);
        nrOfEmployees.setText(Integer.toString(EmployeesViewDAO.getNrOfEmployees()));
        nrOfParkingsOp.setText(Integer.toString(EmployeesViewDAO.getNrOfParkingsOp()));
        nrOfDepartments.setText(Integer.toString(EmployeesViewDAO.getNrOfDepartments()));
        minEmployees.setText(Integer.toString(EmployeesViewDAO.getMinEmployees()));
        maxEmployees.setText(Integer.toString(EmployeesViewDAO.getMaxEmployees()));
        minDepartment.setText(EmployeesViewDAO.getMinDepartment());
        maxDepartment.setText(EmployeesViewDAO.getMaxDepartment());
        averageEmployees.setText(Float.toString(EmployeesViewDAO.getAverageEmployees()));
        minSalary.setText(Float.toString(EmployeesViewDAO.getMinSalary()));
        maxSalary.setText(Float.toString(EmployeesViewDAO.getMaxSalary()));
        medianSalary.setText(Float.toString(EmployeesViewDAO.getMedianSalary()));
        averageSalary.setText(Float.toString(EmployeesViewDAO.getAverageSalary()));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

