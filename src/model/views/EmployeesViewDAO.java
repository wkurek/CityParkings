package model.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.department.Department;
import model.department.DepartmentDAO;
import util.DbHelper;
import util.Validator;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeesViewDAO {

    private static class UserContract {
        public static final String TABLE_NAME = "[dbo].[employees_view]";
        public static final String COLUMN_NAME_ID = "employee_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_SALARY = "salary";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_NUMBER= "number";
        public static final String COLUMN_NAME_DEPARTMENT_NAME = "department_name";
        public static final String COLUMN_NAME_PARKING_ID = "parking_id";
        public static final String COLUMN_NAME_LAST_CONTROL = "last_control";
    }

    public static ObservableList<EmployeesView> getEmployeesViews(String salaryMin, String salaryMax, List<String> countries, List<String> departments)
    {
        String sql = generateSelectWhereQuery(salaryMin, salaryMax, countries, departments);

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<EmployeesView> employeesViewsList = null;
        try {
            employeesViewsList = generateEmployeesViewsList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return employeesViewsList;
    }

    private static String generateSelectWhereQuery(String salaryMin, String salaryMax, List<String> countries, List<String> departments) {
        String sql = generateSelectQuery();
        sql+=" WHERE 1=1";
        if(salaryMin!=null && Validator.isWeightValid(salaryMin)){
            sql+="and salary>="+salaryMin+" ";
        }
        if(salaryMax!=null && Validator.isWeightValid(salaryMax)){
            sql+="and salary<="+salaryMax+" ";
        }
        if(countries.size()!=0&&countries.get(0)!="Select All") {
            sql+="and(";
            for(String s : countries) {
                sql+="country = '"+s+"' or ";
            }
            sql+="1=0) ";
        }
        if(departments.size()!=0&&departments.get(0)!="Select All") {
            sql+="and(";
            for(String s : departments) {
                sql+="department_name = '"+s+"' or ";
            }
            sql+="1=0)";
        }
        return sql;
    }

    private static ObservableList<EmployeesView> generateEmployeesViewsList(CachedRowSet resultSet) throws SQLException{
        ObservableList<EmployeesView> employeesViewsList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            EmployeesView employeesView = generateEmployeesView(resultSet);
            employeesViewsList.add(employeesView);
        }

        return employeesViewsList;
    }

    private static String generateSelectQuery()
    {
        return "SELECT * FROM " + UserContract.TABLE_NAME;
    }
    public static EmployeesView generateEmployeesView(CachedRowSet resultSet) throws SQLException {
        EmployeesView employeesView = new EmployeesView();

        Department department = DepartmentDAO.generateDepartment(resultSet);

        employeesView.setDepartment(department);

        employeesView.setId(resultSet.getInt(UserContract.COLUMN_NAME_ID));
        employeesView.setName(resultSet.getString(UserContract.COLUMN_NAME_NAME));
        employeesView.setSurname(resultSet.getString(UserContract.COLUMN_NAME_SURNAME));
        employeesView.setSalary(resultSet.getFloat(UserContract.COLUMN_NAME_SALARY));
        employeesView.setCountry(resultSet.getString(UserContract.COLUMN_NAME_COUNTRY));
        employeesView.setCity(resultSet.getString(UserContract.COLUMN_NAME_CITY));
        employeesView.setZip_code(resultSet.getString(UserContract.COLUMN_NAME_ZIP_CODE));
        employeesView.setStreet(resultSet.getString(UserContract.COLUMN_NAME_STREET));
        employeesView.setNumber(resultSet.getString(UserContract.COLUMN_NAME_NUMBER));
        employeesView.setParking_id(resultSet.getInt(UserContract.COLUMN_NAME_PARKING_ID));
        employeesView.setLastControl(resultSet.getDate(UserContract.COLUMN_NAME_LAST_CONTROL));

        return employeesView;
    }
}
