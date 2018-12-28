package model.park_ride;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.parking.Parking;

public class ParkRide {

    private Parking parking;
    private BooleanProperty automatic;
    private StringProperty communicationNode;

    public ParkRide(){
        parking = new Parking();
        communicationNode = new SimpleStringProperty();
        automatic = new SimpleBooleanProperty();
    }

    public Parking getParking() { return parking; }

    public void setParking(Parking parking) { this.parking = parking; }

    public String getCommunicationNode() { return communicationNode.get(); }

    public StringProperty communicationNodeProperty() { return communicationNode; }

    public void setCommunicationNode(String communicationNode) { this.communicationNode.set(communicationNode); }

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
