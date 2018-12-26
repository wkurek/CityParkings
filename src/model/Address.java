package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Address {
    private IntegerProperty id;
    private StringProperty zipCode;
    private StringProperty street;
    private StringProperty number;
    private StringProperty countryName;
    private StringProperty countryISO;

    Address() {
        id = new SimpleIntegerProperty();
        zipCode = new SimpleStringProperty();
        street = new SimpleStringProperty();
        number = new SimpleStringProperty();
        countryName = new SimpleStringProperty();
        countryISO = new SimpleStringProperty();
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

    public String getZipCode() {
        return zipCode.get();
    }

    public StringProperty zipCodeProperty() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
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

    public String getCountryName() {
        return countryName.get();
    }

    public StringProperty countryNameProperty() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName.set(countryName);
    }

    public String getCountryISO() {
        return countryISO.get();
    }

    public StringProperty countryISOProperty() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO.set(countryISO);
    }
}

