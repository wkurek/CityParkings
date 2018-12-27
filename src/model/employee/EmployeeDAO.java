package model.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.address.Address;
import model.address.AddressDAO;
import model.country.CountryDAO;
import model.department.Department;
import model.department.DepartmentDAO;
import model.parking.Parking;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class EmployeeDAO {
    private static class EmployeeContract {
        static final String TABLE_NAME = "[dbo].[employees]";
        static final String COLUMN_NAME_ID = "employee_id";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SURNAME = "surname";
        static final String COLUMN_NAME_SALARY = "salary";
        static final String COLUMN_NAME_DEPARTMENT_NAME = "department_name";
        static final String COLUMN_NAME_ADDRESS_ID = "address_id";
        static final String COLUMN_NAME_PARKING_ID = "parking_id";
    }

    public static Employee generateEmployee(CachedRowSet resultSet) throws SQLException {
        Employee employee = new Employee();

        Department department = new Department();
        employee.setDepartment(department);

        Address address = new Address();
        employee.setAddress(address);

        Parking parking = new Parking();
        employee.setParking(parking);

        employee.setId(resultSet.getInt(EmployeeContract.COLUMN_NAME_ID));
        employee.setName(resultSet.getString(EmployeeContract.COLUMN_NAME_NAME));
        employee.setSurname(resultSet.getString(EmployeeContract.COLUMN_NAME_SURNAME));
        employee.setSalary(resultSet.getFloat(EmployeeContract.COLUMN_NAME_SALARY));

        return employee;
    }

    private static ObservableList<Employee> generateEmployeesList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Employee> employeesList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Employee employee = generateEmployee(resultSet);
            employeesList.add(employee);
        }

        return employeesList;
    }

    private static String generateSelectQuery() {
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, " +
                "%s.%s, %s.%s, %s.%s, %s.%s FROM %s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s " +
                "LEFT JOIN %s ON %s.%s=%s.%s",
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_ID,
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_NAME,
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_SURNAME,
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_SALARY,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_COUNTRY,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_ISO,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ID,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_CITY,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ZIP_CODE,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_STREET,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_NUMBER,

                );

    }

    private static String generateSelectWhereQuery(int id) {
        String sql = generateSelectQuery();
        return String.format("%s WHERE %s.%s=%d", sql, EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_ID, id);
    }

    public static ObservableList<Employee> getEmployees() {
        String sql = generateSelectQuery();

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Employee> employeesList = null;

        try {
            employeesList = generateEmployeesList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return employeesList;
    }

    public static Employee getEmployee(int id) {
        String sql = generateSelectWhereQuery(id);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Employee employee = null;

        try {
            employee = generateEmployee(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return employee;
    }

    public static void saveEmployee(Employee employee) {
        if(employee == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (0, '%s', '%s', %.2f, '%s', %d, %d)", EmployeeContract.TABLE_NAME,
                employee.getName(), employee.getSurname(), employee.getSalary(), employee.getDepartment().getDepartmentName(),
                employee.getAddress().getId(), employee.getParking().getParkingId());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void updateEmployee(int id, Employee updatedEmployee) {
        if(updatedEmployee == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("UPDATE %s SET %s = '%s', %s = '%s', %s = %.2f, %s = '%s', %s = %d, %s = %d WHERE %s = %d",
                EmployeeContract.TABLE_NAME,
                EmployeeContract.COLUMN_NAME_NAME, updatedEmployee.getName(),
                EmployeeContract.COLUMN_NAME_SURNAME, updatedEmployee.getSurname(),
                EmployeeContract.COLUMN_NAME_SALARY, updatedEmployee.getSalary(),
                EmployeeContract.COLUMN_NAME_DEPARTMENT_NAME, updatedEmployee.getDepartment().getDepartmentName(),
                EmployeeContract.COLUMN_NAME_ADDRESS_ID, updatedEmployee.getAddress().getId(),
                EmployeeContract.COLUMN_NAME_PARKING_ID, updatedEmployee.getParking().getParkingId(),
                EmployeeContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteEmployee(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = %d",EmployeeContract.TABLE_NAME,
                EmployeeContract.COLUMN_NAME_ID, id);

        DbHelper.executeUpdateQuery(sql);
    }
}
