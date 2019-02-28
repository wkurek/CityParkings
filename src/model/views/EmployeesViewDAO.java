package model.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.department.Department;
import model.department.DepartmentDAO;
import util.DbHelper;
import util.Validator;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeesViewDAO {

    private static class EmployeesViewContract {
        static final String TABLE_NAME = "[dbo].[employees_view]";
        static final String COLUMN_NAME_ID = "employee_id";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SURNAME = "surname";
        static final String COLUMN_NAME_SALARY = "salary";
        static final String COLUMN_NAME_COUNTRY = "country";
        static final String COLUMN_NAME_CITY = "city";
        static final String COLUMN_NAME_ZIP_CODE = "zip_code";
        static final String COLUMN_NAME_STREET = "street";
        static final String COLUMN_NAME_NUMBER= "number";
        static final String COLUMN_NAME_DEPARTMENT_NAME = "department_name";
        static final String COLUMN_NAME_PARKING_ID = "parking_id";
        static final String COLUMN_NAME_LAST_CONTROL = "last_control";
    }

    private static int nrOfEmployees;
    private static int nrOfParkingsOp;
    private static int nrOfDepartments;
    private static int minEmployees;
    private static int maxEmployees;
    private static float averageEmployees;
    private static String minDepartment;
    private static String maxDepartment;
    private static float minSalary;
    private static float maxSalary;
    private static float averageSalary;
    private static float medianSalary;



    public static ObservableList<EmployeesView> getEmployeesViews(String salaryMin, String salaryMax, List<String> countries, List<String> departments)
    {
        String sql = generateSelectQuery() + generateWherePartOfQuery(salaryMin, salaryMax, countries, departments);

        CachedRowSet result = DbHelper.executeQuery(sql);

        ObservableList<EmployeesView> employeesViewsList = FXCollections.observableArrayList();
        try {
            employeesViewsList = generateEmployeesViewsList(result);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return employeesViewsList;
    }

    private static String generateWherePartOfQuery(String salaryMin, String salaryMax, List<String> countries, List<String> departments) {
        String sql=" WHERE 1=1 ";
        if (salaryMin != null && !salaryMin.equals("") && Validator.isWeightValid(salaryMin)) {
            sql+="and "+EmployeesViewContract.COLUMN_NAME_SALARY+">="+salaryMin+" ";
        }
        if (salaryMax != null && !salaryMax.equals("") && Validator.isWeightValid(salaryMax)) {
            sql+="and "+EmployeesViewContract.COLUMN_NAME_SALARY+"<="+salaryMax+" ";
        }
        if(countries.size()!=0&&!countries.get(0).equals("Select All")) {
            sql+="and(";
            for(String s : countries) {
                sql+=EmployeesViewContract.COLUMN_NAME_COUNTRY+" = '"+s+"' or ";
            }
            sql+="1=0) ";
        }
        if(departments.size()!=0&&!departments.get(0).equals("Select All")) {
            sql+="and(";
            for(String s : departments) {
                sql+=EmployeesViewContract.COLUMN_NAME_DEPARTMENT_NAME+" = '"+s+"' or ";
            }
            sql+="1=0) ";
        }
        return sql;
    }
    private static String generateSelectQuery()
    {
        return "SELECT * FROM " + EmployeesViewContract.TABLE_NAME;
    }
    private static ObservableList<EmployeesView> generateEmployeesViewsList(CachedRowSet resultSet) throws SQLException{
        ObservableList<EmployeesView> employeesViewsList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            EmployeesView employeesView = generateEmployeesView(resultSet);
            employeesViewsList.add(employeesView);
        }

        return employeesViewsList;
    }



    private static EmployeesView generateEmployeesView(CachedRowSet resultSet) throws SQLException {
        EmployeesView employeesView = new EmployeesView();

        Department department = DepartmentDAO.generateDepartment(resultSet);

        employeesView.setDepartment(department);

        employeesView.setId(resultSet.getInt(EmployeesViewContract.COLUMN_NAME_ID));
        employeesView.setName(resultSet.getString(EmployeesViewContract.COLUMN_NAME_NAME));
        employeesView.setSurname(resultSet.getString(EmployeesViewContract.COLUMN_NAME_SURNAME));
        employeesView.setSalary(resultSet.getFloat(EmployeesViewContract.COLUMN_NAME_SALARY));
        employeesView.setCountry(resultSet.getString(EmployeesViewContract.COLUMN_NAME_COUNTRY));
        employeesView.setCity(resultSet.getString(EmployeesViewContract.COLUMN_NAME_CITY));
        employeesView.setZip_code(resultSet.getString(EmployeesViewContract.COLUMN_NAME_ZIP_CODE));
        employeesView.setStreet(resultSet.getString(EmployeesViewContract.COLUMN_NAME_STREET));
        employeesView.setNumber(resultSet.getString(EmployeesViewContract.COLUMN_NAME_NUMBER));
        employeesView.setParkingID(resultSet.getInt(EmployeesViewContract.COLUMN_NAME_PARKING_ID));
        employeesView.setLastControl(resultSet.getDate(EmployeesViewContract.COLUMN_NAME_LAST_CONTROL));

        return employeesView;
    }

    private static int getDistinctNumber(String wherePartOfQuery, String distinctColumnName){
        String sql = "SELECT COUNT (DISTINCT "+distinctColumnName+") AS x FROM "+
                    EmployeesViewContract.TABLE_NAME+wherePartOfQuery;

        CachedRowSet row = DbHelper.executeQuery(sql);
        int result;
        try {
            row.next();
            result = row.getInt("x");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            result = 0;
        }
        return result;
    }
    private static void generateMinMaxEmployees(String wherePartOfQuery)
    {
        String sql = "SELECT COUNT("+EmployeesViewContract.COLUMN_NAME_DEPARTMENT_NAME+") AS x, "+EmployeesViewContract.COLUMN_NAME_DEPARTMENT_NAME+
                " FROM "+EmployeesViewContract.TABLE_NAME+wherePartOfQuery+" GROUP BY "+EmployeesViewContract.COLUMN_NAME_DEPARTMENT_NAME+
                " ORDER BY x ";
        CachedRowSet row = DbHelper.executeQuery(sql+"ASC");
        try {
            if(row.next()) {
                minEmployees = row.getInt("x");
                minDepartment = row.getString(EmployeesViewContract.COLUMN_NAME_DEPARTMENT_NAME);
            }
            else
            {
                minEmployees = 0;
                minDepartment = "";
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        row = DbHelper.executeQuery(sql+"DESC");
        try {
            if(row.next()) {
                maxEmployees = row.getInt("x");
                maxDepartment = row.getString(EmployeesViewContract.COLUMN_NAME_DEPARTMENT_NAME);
            }
            else
            {
                maxEmployees = 0;
                maxDepartment = "";
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void generateSalaries(List<EmployeesView> employeesViews)
    {
        if(employeesViews==null||employeesViews.size()==0) {
            minSalary = maxSalary = medianSalary = averageSalary = 0;
            return;
        }
        List<Float> salaries = new ArrayList<>();
        for(EmployeesView e : employeesViews)
            salaries.add(e.getSalary());
        if(salaries.size()==0)
        {
            minSalary=maxSalary=medianSalary=averageSalary=0;
            return;
        }
        salaries.sort(Comparator.naturalOrder());
        minSalary = salaries.get(0);
        maxSalary = salaries.get(salaries.size()-1);
        medianSalary = salaries.size()%2==1 ? salaries.get(salaries.size()/2) : (salaries.get(salaries.size()/2-1)+salaries.get(salaries.size()/2))/2;
        averageSalary = (float)salaries.stream().mapToDouble(a->a).average().orElse(0.0);
    }
    public static void generateStatistics(String salaryMin, String salaryMax, List<String> countries, List<String> departments) {
        ObservableList<EmployeesView> employeesViews = getEmployeesViews(salaryMin, salaryMax, countries, departments);
        nrOfEmployees = employeesViews.size();

        String sql = generateWherePartOfQuery(salaryMin, salaryMax, countries, departments);
        nrOfParkingsOp = getDistinctNumber(sql, EmployeesViewContract.COLUMN_NAME_PARKING_ID);
        nrOfDepartments = getDistinctNumber(sql, EmployeesViewContract.COLUMN_NAME_DEPARTMENT_NAME);
        generateMinMaxEmployees(sql);
        if(nrOfDepartments==0)
            averageEmployees=0;
        else
            averageEmployees = (float)nrOfEmployees/(float)nrOfDepartments;

        generateSalaries(employeesViews);

    }

    public static void generateStatistics(List<EmployeesView> employeesViews) {
        nrOfEmployees = employeesViews.size();
        nrOfParkingsOp = employeesViews.stream().map(e -> String.valueOf(e.getParkingID())).collect(Collectors.toSet()).size();
        nrOfDepartments = employeesViews.stream().map(e -> e.getDepartment().getDepartmentName()).collect(Collectors.toSet()).size();
        Map<String, Long> counts = employeesViews.stream().collect(Collectors.groupingBy(e -> e.getDepartment().getDepartmentName(), Collectors.counting()));
        Map.Entry<String, Long> max = null;
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            if (max == null || entry.getValue().compareTo(max.getValue()) > 0) {
                max = entry;
            }
        }
        assert max != null;
        maxEmployees = max.getValue().intValue();
        maxDepartment = max.getKey();
        Map.Entry<String, Long> min = null;
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            if (min == null || entry.getValue().compareTo(min.getValue()) < 0) {
                min = entry;
            }
        }
        assert min != null;
        minEmployees = min.getValue().intValue();
        minDepartment = min.getKey();
        if (nrOfDepartments == 0)
            averageEmployees = 0;
        else
            averageEmployees = (float) nrOfEmployees / (float) nrOfDepartments;

        generateSalaries(employeesViews);
    }

    public static int getNrOfEmployees() {
        return nrOfEmployees;
    }

    public static int getNrOfParkingsOp() {
        return nrOfParkingsOp;
    }

    public static int getNrOfDepartments() {
        return nrOfDepartments;
    }

    public static int getMinEmployees() {
        return minEmployees;
    }

    public static int getMaxEmployees() {
        return maxEmployees;
    }

    public static float getAverageEmployees() {
        return averageEmployees;
    }

    public static String getMinDepartment() {
        return minDepartment;
    }

    public static String getMaxDepartment() {
        return maxDepartment;
    }

    public static float getMinSalary() {
        return minSalary;
    }

    public static float getMaxSalary() {
        return maxSalary;
    }

    public static float getAverageSalary() {
        return averageSalary;
    }

    public static float getMedianSalary() {
        return medianSalary;
    }
}
