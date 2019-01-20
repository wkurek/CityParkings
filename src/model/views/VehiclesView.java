package model.views;

import javafx.beans.property.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Date;

public class VehiclesView {
    private IntegerProperty id;
    private StringProperty plateNumber;
    private FloatProperty height;
    private FloatProperty weight;
    private StringProperty engineType;
    private StringProperty ownerName;
    private StringProperty ownerSurname;
    private StringProperty phoneNumber;
    private StringProperty country;
    private StringProperty city;
    private StringProperty zip_code;
    private StringProperty street;
    private StringProperty number;
    private SimpleObjectProperty<Integer> parkingID;
    private SimpleObjectProperty<Date> parkDateTime;

    public VehiclesView()
    {
        id = new SimpleIntegerProperty();
        plateNumber = new SimpleStringProperty();
        height = new SimpleFloatProperty();
        weight = new SimpleFloatProperty();
        height = new SimpleFloatProperty();
        engineType = new SimpleStringProperty();
        ownerName = new SimpleStringProperty();
        ownerSurname = new SimpleStringProperty();
        phoneNumber = new SimpleStringProperty();
        country = new SimpleStringProperty();
        city = new SimpleStringProperty();
        zip_code = new SimpleStringProperty();
        street = new SimpleStringProperty();
        number = new SimpleStringProperty();
        parkingID = new SimpleObjectProperty<>();
        parkDateTime = new SimpleObjectProperty<>();
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

    public String getPlateNumber() {
        return plateNumber.get();
    }

    public StringProperty plateNumberProperty() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber.set(plateNumber);
    }

    public float getHeight() {
        return height.get();
    }

    public FloatProperty heightProperty() {
        return height;
    }

    public void setHeight(float height) {
        this.height.set(height);
    }

    public float getWeight() {
        return weight.get();
    }

    public FloatProperty weightProperty() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight.set(weight);
    }

    public String getEngineType() {
        return engineType.get();
    }

    public StringProperty engineTypeProperty() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType.set(engineType);
    }

    public String getOwnerName() {
        return ownerName.get();
    }

    public StringProperty ownerNameProperty() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName.set(ownerName);
    }

    public String getOwnerSurname() {
        return ownerSurname.get();
    }

    public StringProperty ownerSurnameProperty() {
        return ownerSurname;
    }

    public void setOwnerSurname(String ownerSurname) {
        this.ownerSurname.set(ownerSurname);
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

    public int getParkingID() {
        return parkingID.get();
    }

    public SimpleObjectProperty<Integer> parkingIDProperty() {
        return parkingID;
    }

    public void setParkingID(Integer parkingID) {
        this.parkingID.set(parkingID);
    }

    public SimpleObjectProperty<DateTime> getParkDateTime() {
        return new SimpleObjectProperty<>(new DateTime(parkDateTime.get(), DateTimeZone.UTC));
    }

    public SimpleObjectProperty<Date> parkDateTimeProperty() {
        return parkDateTime;
    }

    public void setParkDateTime(Date parkDateTime) {
        this.parkDateTime.set(parkDateTime);
    }
}
