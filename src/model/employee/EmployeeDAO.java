package model.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.address.Address;
import model.address.AddressDAO;
import model.country.CountryDAO;
import model.department.Department;
import model.department.DepartmentDAO;
import model.location.LocationDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class EmployeeDAO {
    public static class EmployeeContract {
        public static final String TABLE_NAME = "[dbo].[employees]";
        public static final String COLUMN_NAME_ID = "employee_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_SALARY = "salary";
        public static final String COLUMN_NAME_DEPARTMENT_NAME = "department_name";
        public static final String COLUMN_NAME_ADDRESS_ID = "address_id";
        public static final String COLUMN_NAME_PARKING_ID = "parking_id";
    }

    private static Employee generateEmployee(CachedRowSet resultSet) throws SQLException {
        Employee employee = new Employee();

        Department department = DepartmentDAO.generateDepartment(resultSet);
        employee.setDepartment(department);

        Address address = AddressDAO.generateAddress(resultSet);
        employee.setAddress(address);

        Parking parking = ParkingDAO.generateParking(resultSet);
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
        return String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, " +
                        "%s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s FROM %s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s " +
                        "LEFT JOIN %s ON %s.%s=%s.%s LEFT JOIN %s ON %s.%s=%s.%s",
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_ID,
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_NAME,
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_SURNAME,
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_SALARY,

                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_ID,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_STANDARD_LOTS,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_DISABLED_LOTS,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_IS_ROOF,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_IS_GUARDED,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_LAST_CONTROL,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_WEIGHT,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_HEIGHT,

                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_LATITUDE,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_LONGITUDE,

                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_COUNTRY,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_ISO,

                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ID,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_CITY,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ZIP_CODE,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_STREET,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_NUMBER,

                DepartmentDAO.DepartmentContract.TABLE_NAME, DepartmentDAO.DepartmentContract.COLUMN_NAME_DEPARTMENT_NAME,

                EmployeeContract.TABLE_NAME, DepartmentDAO.DepartmentContract.TABLE_NAME,
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_DEPARTMENT_NAME,
                DepartmentDAO.DepartmentContract.TABLE_NAME, DepartmentDAO.DepartmentContract.COLUMN_NAME_DEPARTMENT_NAME,

                AddressDAO.AddressContract.TABLE_NAME,
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_ADDRESS_ID,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_ID,

                CountryDAO.CountryContract.TABLE_NAME,
                AddressDAO.AddressContract.TABLE_NAME, AddressDAO.AddressContract.COLUMN_NAME_COUNTRY,
                CountryDAO.CountryContract.TABLE_NAME, CountryDAO.CountryContract.COLUMN_NAME_COUNTRY,

                ParkingDAO.ParkingContract.TABLE_NAME,
                EmployeeContract.TABLE_NAME, EmployeeContract.COLUMN_NAME_PARKING_ID,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_ID,

                LocationDAO.LocationContract.TABLE_NAME,
                ParkingDAO.ParkingContract.TABLE_NAME, ParkingDAO.ParkingContract.COLUMN_NAME_LOCATION_ID,
                LocationDAO.LocationContract.TABLE_NAME, LocationDAO.LocationContract.COLUMN_NAME_ID

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

        String sql = String.format("INSERT INTO %s VALUES ('%s', '%s', %.2f, '%s', %d, %d)", EmployeeContract.TABLE_NAME,
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
