package controller;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
    private TextField salaryMinInput;
    @FXML
    private TextField salaryMaxInput;
    private List<CheckMenuItem> countryItems;
    private List<CheckMenuItem> departmentItems;
    private List<CheckMenuItem> columnItems;

    private List<TableColumn> columns;
    private Stage stage;

    private ObservableList<EmployeesView> employeesViewList;
  //  private Task<ObservableList<EmployeesView>> employeesViewLoadTask;

    public EmployeesTabController()
    {
        columns = new ArrayList<>();
        countryItems = new ArrayList<>();
        departmentItems = new ArrayList<>();
        columnItems = new ArrayList<>();
        employeesViewList = FXCollections.observableArrayList();
       // employeesViewLoadTask = generateEmployeesViewsLoadTask();
    }

    @FXML
    private void initialize()
    {
          menuButtonsSet();
          autoShowOff();
          generateTableColumns();
          setUpTable();
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

//    private Task<ObservableList<EmployeesView>> generateEmployeesViewsLoadTask() {
//        Task<ObservableList<EmployeesView>> task = new Task<>() {
//            @Override
//            protected ObservableList<EmployeesView> call() {
//                return EmployeesViewDAO.getEmployeesViews(salaryMinInput.getText(), salaryMaxInput.getText(),
//                                                        RootController.selectedMenuItemsToStringList(countryMenuButton.getItems()),
//                                                        RootController.selectedMenuItemsToStringList(departmentMenuButton.getItems()));
//            }
//        };
//
//        task.setOnSucceeded(event -> {
//            employeesViewList.clear();
//            employeesViewList.addAll(task.getValue());
//        });
//
//        task.setOnFailed(event -> {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.initOwner(stage);
//            alert.setTitle("SQL Error");
//            alert.setHeaderText(event.getSource().getException().getMessage());
//            alert.show();
//        });
//
//        return task;
//    }
//    private void scheduleLoadTask(Task task) {
//        if(task != null && task.isRunning()) task.cancel();
//
//        new Thread(task).start();
//    }
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
        parkingID.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    int value = item;
                    if (value == 0) {
                        setText("");
                    } else {
                        setText(Integer.toString(value));
                    }
                }
            }
        });
        parkingID.setCellValueFactory(param->param.getValue().parkingIDProperty().asObject());
        columns.add(parkingID);
        TableColumn<EmployeesView, Date> lastControl = new TableColumn<>(COLUMN_NAMES.get(11));
        lastControl.setCellValueFactory(param->param.getValue().lastControlProperty());
        columns.add(lastControl);
    }
    @FXML
    public void onFilterButtonClicked() {
        setUpTable();
    }
    private void setUpTable()
    {
        ReportsController.setColumns(employeesViewTable, columns, columnMenuButton);
        employeesViewList = EmployeesViewDAO.getEmployeesViews(salaryMinInput.getText(), salaryMaxInput.getText(),
                ReportsController.selectedMenuItemsToStringList(countryMenuButton.getItems()),
                ReportsController.selectedMenuItemsToStringList(departmentMenuButton.getItems()));
        employeesViewTable.setItems(employeesViewList);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

