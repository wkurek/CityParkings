package model.parking;

import javafx.beans.property.*;
import model.location.Location;

import java.sql.Date;

public class Parking {

    private IntegerProperty parkingId;
    private IntegerProperty standardLots;
    private IntegerProperty disabledLots;
    private BooleanProperty roofed;
    private BooleanProperty guarded;
    private SimpleObjectProperty<Date> lastControl;
    private FloatProperty maxWeight;
    private FloatProperty maxHeight;
    private Location location;

    public Parking(){
        parkingId = new SimpleIntegerProperty();
        standardLots = new SimpleIntegerProperty();
        disabledLots = new SimpleIntegerProperty();
        roofed = new SimpleBooleanProperty();
        guarded = new SimpleBooleanProperty();
        lastControl = new SimpleObjectProperty<>();
        maxWeight = new SimpleFloatProperty();
        maxHeight = new SimpleFloatProperty();
        location = new Location();
    }

    public int getParkingId() { return parkingId.get(); }

    public IntegerProperty parkingIdProperty() { return parkingId; }

    public void setParkingId(int parkingId) { this.parkingId.set(parkingId); }

    public int getStandardLots() { return standardLots.get(); }

    public IntegerProperty standardLotsProperty() { return standardLots; }

    public void setStandardLots(int standardLots) { this.standardLots.set(standardLots); }

    public int getDisabledLots() { return disabledLots.get(); }

    public IntegerProperty disabledLotsProperty() { return disabledLots; }

    public void setDisabledLots(int disabledLots) { this.disabledLots.set(disabledLots); }

    public Date getLastControl() { return lastControl.get(); }

    public SimpleObjectProperty<Date> lastControlProperty() { return lastControl; }

    public void setLastControl(Date lastControl) { this.lastControl.set(lastControl); }

    public float getMaxWeight() { return maxWeight.get(); }

    public FloatProperty maxWeightProperty() { return maxWeight; }

    public void setMaxWeight(float maxWeight) { this.maxWeight.set(maxWeight); }

    public float getMaxHeight() { return maxHeight.get(); }

    public FloatProperty maxHeightProperty() { return maxHeight; }

    public void setMaxHeight(float maxHeight) { this.maxHeight.set(maxHeight); }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    public boolean isGuarded() {
        return guarded.get();
    }

    public BooleanProperty guardedProperty() {
        return guarded;
    }

    public void setGuarded(boolean guarded) {
        this.guarded.set(guarded);
    }

    public boolean isRoofed() {
        return roofed.get();
    }

    public BooleanProperty roofedProperty() {
        return roofed;
    }

    public void setRoofed(boolean roofed) {
        this.roofed.set(roofed);
    }

    @Override
    public String toString() {

        return String.format("%d", getParkingId());
    }
}
