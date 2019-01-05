package model.views;

import javafx.beans.property.*;
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
    private StringProperty country;
    private StringProperty city;
    private StringProperty zip_code;
    private StringProperty street;
    private StringProperty number;
    private Department department;
    private IntegerProperty parkingID;
    private SimpleObjectProperty<Date> lastControl;


    public EmployeesView() {
        id = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        surname = new SimpleStringProperty();
        salary = new SimpleFloatProperty();
        country = new SimpleStringProperty();
        city = new SimpleStringProperty();
        zip_code = new SimpleStringProperty();
        street = new SimpleStringProperty();
        number = new SimpleStringProperty();
        department = new Department();
        parkingID = new SimpleIntegerProperty();
        lastControl = new SimpleObjectProperty<>();
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

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getZip_code() {
        return zip_code.get();
    }

    public StringProperty zip_codeProperty() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code.set(zip_code);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getParkingID() {
        return parkingID.get();
    }

    public IntegerProperty parkingIDProperty() {
        return parkingID;
    }

    public void setParkingID(int parking_id) {
        this.parkingID.set(parking_id);
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
