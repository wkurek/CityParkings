package model.views;

import javafx.beans.property.*;


import java.sql.Date;

public class UsersView {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty surname;
    private StringProperty phoneNumber;
    private StringProperty country;
    private StringProperty city;
    private StringProperty zip_code;
    private StringProperty street;
    private StringProperty number;
    private IntegerProperty numberOfVehicles;
    private IntegerProperty cardID;
    private SimpleObjectProperty<Date> expirationDate;

    public UsersView()
    {
        id = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        surname = new SimpleStringProperty();
        phoneNumber = new SimpleStringProperty();
        country = new SimpleStringProperty();
        city = new SimpleStringProperty();
        zip_code = new SimpleStringProperty();
        street = new SimpleStringProperty();
        number = new SimpleStringProperty();
        numberOfVehicles = new SimpleIntegerProperty();
        cardID = new SimpleIntegerProperty();
        expirationDate = new SimpleObjectProperty<>();
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

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
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

    public int getNumberOfVehicles() {
        return numberOfVehicles.get();
    }

    public IntegerProperty numberOfVehiclesProperty() {
        return numberOfVehicles;
    }

    public void setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles.set(numberOfVehicles);
    }

    public int getCardID() {
        return cardID.get();
    }

    public IntegerProperty cardIDProperty() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID.set(cardID);
    }

    public Date getExpirationDate() {
        return expirationDate.get();
    }

    public SimpleObjectProperty<Date> expirationDateProperty() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate.set(expirationDate);
    }
}
