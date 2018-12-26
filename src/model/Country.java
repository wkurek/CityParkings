package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Country {
    private StringProperty name;
    private StringProperty iso;

    Country() {
        name = new SimpleStringProperty();
        iso = new SimpleStringProperty();
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getIso() {
        return iso.get();
    }

    public StringProperty isoProperty() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso.set(iso);
    }
}
