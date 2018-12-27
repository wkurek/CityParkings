package model.kiss_ride;

import javafx.beans.property.*;
import model.parking.Parking;

import java.sql.Date;

public class KissRide {

    private Parking parking;
    private IntegerProperty maxStopMinutes;
    private BooleanProperty areGates;

    public KissRide(){
        parking = new Parking();
        maxStopMinutes = new SimpleIntegerProperty();
        areGates = new SimpleBooleanProperty();
    }

    public Parking getParking() { return parking; }

    public void setParking(Parking parking) { this.parking = parking; }

    public boolean isAreGates() { return areGates.get(); }

    public BooleanProperty areGatesProperty() { return areGates; }

    public void setAreGates(boolean areGates) { this.areGates.set(areGates); }

    public int getMaxStopMinutes() {
        return maxStopMinutes.get();
    }

    public IntegerProperty maxStopMinutesProperty() {
        return maxStopMinutes;
    }

    public void setMaxStopMinutes(int maxStopMinutes) {
        this.maxStopMinutes.set(maxStopMinutes);
    }
}
