package model.location;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Location {

    private IntegerProperty locationId;
    private FloatProperty latitude;
    private FloatProperty longitude;

    public Location(){
        locationId = new SimpleIntegerProperty();
        latitude = new SimpleFloatProperty();
        longitude = new SimpleFloatProperty();
    }

    public int getLocationId() {
        return locationId.get();
    }

    public IntegerProperty locationIdProperty() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId.set(locationId);
    }

    public float getLatitude() { return latitude.get(); }

    public FloatProperty latitudeProperty() { return latitude; }

    public void setLatitude(float latitude) { this.latitude.set(latitude); }

    public float getLongitude() { return longitude.get(); }

    public FloatProperty longitudeProperty() { return longitude; }

    public void setLongitude(float longitude) { this.longitude.set(longitude); }

}
