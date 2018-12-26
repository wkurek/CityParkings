package model.location;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Location {

    private FloatProperty latitude;
    private FloatProperty longitude;

    public Location(){

        latitude = new SimpleFloatProperty();
        longitude = new SimpleFloatProperty();
    }

    public float getLatitude() { return latitude.get(); }

    public FloatProperty latitudeProperty() { return latitude; }

    public void setLatitude(float latitude) { this.latitude.set(latitude); }

    public float getLongitude() { return longitude.get(); }

    public FloatProperty longitudeProperty() { return longitude; }

    public void setLongitude(float longitude) { this.longitude.set(longitude); }

}
