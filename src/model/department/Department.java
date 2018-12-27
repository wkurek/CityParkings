package model.department;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Department {

    private StringProperty departmentName;

    public Department(){ departmentName = new SimpleStringProperty(); }

    public String getDepartmentName() { return departmentName.get(); }

    public StringProperty departmentNameProperty() { return departmentName; }

    public void setDepartmentName(String departmentName) { this.departmentName.set(departmentName); }
}
