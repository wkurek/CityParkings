package model.estate_parking;

import model.estate.Estate;
import model.parking.Parking;

public class EstateParking {

    private Parking parking;
    private Estate estate;

    public EstateParking(){
        parking = new Parking();
        estate = new Estate();
    }

    public Parking getParking() { return parking; }

    public void setParking(Parking parking) { this.parking = parking; }

    public Estate getEstate() { return estate; }

    public void setEstate(Estate estate) { this.estate = estate; }
}
