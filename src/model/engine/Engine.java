package model.engine;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Engine {
    private StringProperty type;

    public Engine() {
        type = new SimpleStringProperty();
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }
}
