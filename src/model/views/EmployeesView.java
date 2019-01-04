package model.views;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import model.address.Address;
import model.department.Department;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class EmployeesView {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty surname;
    private FloatProperty salary;
    private Address address;
    private Department department;
    private IntegerProperty parking_id;
    private SimpleObjectProperty<Date> lastControl;


    public EmployeesView() {

    }
}
