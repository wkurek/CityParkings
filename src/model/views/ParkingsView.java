package model.views;

import javafx.beans.property.*;

import java.sql.Date;

public class ParkingsView {
    private IntegerProperty id;
    private IntegerProperty standardLots;
    private IntegerProperty disabledLots;
    private IntegerProperty occupiedLots;
    private BooleanProperty roofed;
    private BooleanProperty guarded;
    private SimpleObjectProperty<Date> lastControl;
    private FloatProperty maxWeight;
    private FloatProperty maxHeight;
    private IntegerProperty locationID;
    private StringProperty parkType;
    private BooleanProperty zonePaid;
    private StringProperty estateName;
    private BooleanProperty gated;
    private IntegerProperty maxStopMinutes;
    private StringProperty communicationNode;
    private BooleanProperty automatic;

    public ParkingsView() {
        id = new SimpleIntegerProperty();
        standardLots = new SimpleIntegerProperty();
        disabledLots = new SimpleIntegerProperty();
        occupiedLots = new SimpleIntegerProperty();
        roofed = new SimpleBooleanProperty();
        guarded = new SimpleBooleanProperty();
        lastControl = new SimpleObjectProperty<>();
        maxWeight = new SimpleFloatProperty();
        maxHeight = new SimpleFloatProperty();
        locationID = new SimpleIntegerProperty();
        parkType = new SimpleStringProperty();
        zonePaid = new SimpleBooleanProperty();
        estateName = new SimpleStringProperty();
        gated = new SimpleBooleanProperty();
        maxStopMinutes = new SimpleIntegerProperty();
        communicationNode = new SimpleStringProperty();
        automatic = new SimpleBooleanProperty();
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

    public int getStandardLots() {
        return standardLots.get();
    }

    public IntegerProperty standardLotsProperty() {
        return standardLots;
    }

    public void setStandardLots(int standardLots) {
        this.standardLots.set(standardLots);
    }

    public int getDisabledLots() {
        return disabledLots.get();
    }

    public IntegerProperty disabledLotsProperty() {
        return disabledLots;
    }

    public void setDisabledLots(int disabledLots) {
        this.disabledLots.set(disabledLots);
    }

    public int getOccupiedLots() {
        return occupiedLots.get();
    }

    public IntegerProperty occupiedLotsProperty() {
        return occupiedLots;
    }

    public void setOccupiedLots(int occupiedLots) {
        this.occupiedLots.set(occupiedLots);
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

    public boolean isGuarded() {
        return guarded.get();
    }

    public BooleanProperty guardedProperty() {
        return guarded;
    }

    public void setGuarded(boolean guarded) {
        this.guarded.set(guarded);
    }

    public Date getLastControl() {
        return lastControl.get();
    }

    public SimpleObjectProperty<Date> lastControlProperty() {
        return lastControl;
    }

    public void setLastControl(Date lastControl) {
        this.lastControl.set(lastControl);
    }

    public float getMaxWeight() {
        return maxWeight.get();
    }

    public FloatProperty maxWeightProperty() {
        return maxWeight;
    }

    public void setMaxWeight(float maxWeight) {
        this.maxWeight.set(maxWeight);
    }

    public float getMaxHeight() {
        return maxHeight.get();
    }

    public FloatProperty maxHeightProperty() {
        return maxHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight.set(maxHeight);
    }

    public int getLocationID() {
        return locationID.get();
    }

    public IntegerProperty locationIDProperty() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID.set(locationID);
    }

    public String getParkType() {
        return parkType.get();
    }

    public StringProperty parkTypeProperty() {
        return parkType;
    }

    public void setParkType(String parkType) {
        this.parkType.set(parkType);
    }

    public boolean isZonePaid() {
        return zonePaid.get();
    }

    public BooleanProperty zonePaidProperty() {
        return zonePaid;
    }

    public void setZonePaid(boolean zonePaid) {
        this.zonePaid.set(zonePaid);
    }

    public String getEstateName() {
        return estateName.get();
    }

    public StringProperty estateNameProperty() {
        return estateName;
    }

    public void setEstateName(String estateName) {
        this.estateName.set(estateName);
    }

    public boolean isGated() {
        return gated.get();
    }

    public BooleanProperty gatedProperty() {
        return gated;
    }

    public void setGated(boolean gated) {
        this.gated.set(gated);
    }

    public int getMaxStopMinutes() {
        return maxStopMinutes.get();
    }

    public IntegerProperty maxStopMinutesProperty() {
        return maxStopMinutes;
    }

    public void setMaxStopMinutes(int maxStopMinutes) {
        this.maxStopMinutes.set(maxStopMinutes);
    }

    public String getCommunicationNode() {
        return communicationNode.get();
    }

    public StringProperty communicationNodeProperty() {
        return communicationNode;
    }

    public void setCommunicationNode(String communicationNode) {
        this.communicationNode.set(communicationNode);
    }

    public boolean isAutomatic() {
        return automatic.get();
    }

    public BooleanProperty automaticProperty() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic.set(automatic);
    }
}
