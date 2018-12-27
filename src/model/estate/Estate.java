package model.estate;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Estate {

    private StringProperty estateName;

    public Estate(){ estateName = new SimpleStringProperty(); }

    public String getEstateName() { return estateName.get(); }

    public StringProperty estateNameProperty() { return estateName; }

    public void setEstateName(String estateName) { this.estateName.set(estateName); }
}
