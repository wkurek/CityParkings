package model.park;

import javafx.beans.property.SimpleObjectProperty;
import model.parking.Parking;
import model.vehicle.Vehicle;

import java.sql.Date;

public class Park {

    private Vehicle vehicle;
    private Parking parking;
    private SimpleObjectProperty<Date> dateTime;

    public Park(){
        vehicle = new Vehicle();
        parking = new Parking();
        dateTime = new SimpleObjectProperty<>();
    }

    public Vehicle getVehicle() { return vehicle; }

    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public Parking getParking() { return parking; }

    public void setParking(Parking parking) { this.parking = parking; }

    public Date getDateTime() { return dateTime.get(); }

    public SimpleObjectProperty<Date> dateTimeProperty() { return dateTime; }

    public Date getTimeProperty()
    {
        return dateTime.get();
    }


    public void setDateTime(Date dateTime) { this.dateTime.set(dateTime); }
}
