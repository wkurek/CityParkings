package model.city_parking;

import javafx.beans.property.*;
import model.parking.Parking;

public class CityParking {

    private Parking parking;
    private StringProperty parkType;
    private BooleanProperty isZonePaid;

    public CityParking(){
        parking = new Parking();
        parkType = new SimpleStringProperty();
        isZonePaid = new SimpleBooleanProperty();
    }

    public Parking getParking() { return parking; }

    public void setParking(Parking parking) { this.parking = parking; }

    public String getParkType() { return parkType.get(); }

    public StringProperty parkTypeProperty() { return parkType; }

    public void setParkType(String parkType) { this.parkType.set(parkType); }

    public boolean isIsZonePaid() { return isZonePaid.get(); }

    public BooleanProperty isZonePaidProperty() { return isZonePaid; }

    public void setIsZonePaid(boolean isZonePaid) { this.isZonePaid.set(isZonePaid); }
}
