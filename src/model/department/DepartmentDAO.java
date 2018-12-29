package model.department;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DbHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class DepartmentDAO {
    public static class DepartmentContract {
        public static final String TABLE_NAME = "[dbo].[departments]";
        public static final String COLUMN_NAME_DEPARTMENT_NAME = "department_name";
    }

    public static Department generateDepartment(CachedRowSet resultSet) throws SQLException {
        Department department = new Department();
        department.setDepartmentName(resultSet.getString(DepartmentContract.COLUMN_NAME_DEPARTMENT_NAME));

        return department;
    }

    private static ObservableList<Department> generateDepartmentList(CachedRowSet resultSet) throws SQLException {
        ObservableList<Department> departmentList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            Department department = generateDepartment(resultSet);
            departmentList.add(department);
        }

        return departmentList;
    }

    public static ObservableList<Department> getDepartment() {
        String sql = String.format("SELECT TOP(10) [department_name] FROM %s", DepartmentContract.TABLE_NAME);
        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<Department> departmentList = null;

        try {
            departmentList = generateDepartmentList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return departmentList;
    }

    public static Department getDepartment(String departmentName) {
        String sql = String.format("SELECT * FROM %s WHERE %s=%s", DepartmentContract.TABLE_NAME,
                DepartmentContract.COLUMN_NAME_DEPARTMENT_NAME, departmentName);

        CachedRowSet result = DbHelper.executeQuery(sql);

        Department department = null;

        try {
            department = generateDepartment(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return department;
    }

    public static void saveDepartment(Department department) {
        if(department == null) {
            System.err.println("Empty object.");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES ('%s')", DepartmentContract.TABLE_NAME, department.getDepartmentName());

        DbHelper.executeUpdateQuery(sql);
    }

    public static void deleteDepartment(String departmentName) {
        String sql = String.format("DELETE FROM %s WHERE %s = %s", DepartmentContract.TABLE_NAME,
                DepartmentContract.COLUMN_NAME_DEPARTMENT_NAME, departmentName);

        DbHelper.executeUpdateQuery(sql);
    }
}
