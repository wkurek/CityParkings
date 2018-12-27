package model.kiss_ride;

import javafx.beans.property.*;
import model.parking.Parking;

import java.sql.Date;

public class KissRide {

    private Parking parking;
    private SimpleObjectProperty<Date> maxStop;
    private BooleanProperty areGates;

    public KissRide(){
        parking = new Parking();
        maxStop = new SimpleObjectProperty<>();
        areGates = new SimpleBooleanProperty();
    }

    public Parking getParking() { return parking; }

    public void setParking(Parking parking) { this.parking = parking; }

    public Date getMaxStop() { return maxStop.get(); }

    public SimpleObjectProperty<Date> maxStopProperty() { return maxStop; }

    public void setMaxStop(Date maxStop) { this.maxStop.set(maxStop); }

    public boolean isAreGates() { return areGates.get(); }

    public BooleanProperty areGatesProperty() { return areGates; }

    public void setAreGates(boolean areGates) { this.areGates.set(areGates); }
}
