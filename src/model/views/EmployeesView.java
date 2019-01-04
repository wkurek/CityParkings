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

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public float getSalary() {
        return salary.get();
    }

    public FloatProperty salaryProperty() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary.set(salary);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getParking_id() {
        return parking_id.get();
    }

    public IntegerProperty parking_idProperty() {
        return parking_id;
    }

    public void setParking_id(int parking_id) {
        this.parking_id.set(parking_id);
    }

    public Date getLastControl() {
        return lastControl.get();
    }

    public SimpleObjectProperty<Date> lastControlProperty() {
        return lastControl;
    }

    public void setLastControl(Date lastControl) {
        this.lastControl.set(lastControl);
    }
}
