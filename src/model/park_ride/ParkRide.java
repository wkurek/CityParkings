package model.park_ride;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.parking.Parking;

public class ParkRide {

    private Parking parking;
    private BooleanProperty isAutomatic;
    private StringProperty communicationNode;

    public ParkRide(){
        parking = new Parking();
        communicationNode = new SimpleStringProperty();
        isAutomatic = new SimpleBooleanProperty();
    }

    public Parking getParking() { return parking; }

    public void setParking(Parking parking) { this.parking = parking; }

    public boolean isIsAutomatic() { return isAutomatic.get(); }

    public BooleanProperty isAutomaticProperty() { return isAutomatic; }

    public void setIsAutomatic(boolean isAutomatic) { this.isAutomatic.set(isAutomatic); }

    public String getCommunicationNode() { return communicationNode.get(); }

    public StringProperty communicationNodeProperty() { return communicationNode; }

    public void setCommunicationNode(String communicationNode) { this.communicationNode.set(communicationNode); }
}
