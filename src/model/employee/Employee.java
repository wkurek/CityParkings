package model.employee;

import javafx.beans.property.*;
import model.address.Address;
import model.department.Department;
import model.parking.Parking;

public class Employee {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty surname;
    private FloatProperty salary;
    private Department department;
    private Address address;
    private Parking parking;

    public Employee(){
        id = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        surname = new SimpleStringProperty();
        salary = new SimpleFloatProperty();
        department = new Department();
        address = new Address();
        parking = new Parking();
    }

    public int getId() { return id.get(); }

    public IntegerProperty idProperty() { return id; }

    public void setId(int id) { this.id.set(id); }

    public String getName() { return name.get(); }

    public StringProperty nameProperty() { return name; }

    public void setName(String name) { this.name.set(name); }

    public String getSurname() { return surname.get(); }

    public StringProperty surnameProperty() { return surname; }

    public void setSurname(String surname) { this.surname.set(surname); }

    public float getSalary() { return salary.get(); }

    public FloatProperty salaryProperty() { return salary; }

    public void setSalary(float salary) { this.salary.set(salary); }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }

    public Parking getParking() { return parking; }

    public void setParking(Parking parking) { this.parking = parking; }
}
