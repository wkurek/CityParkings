package model;

import javafx.beans.property.*;
import model.engine.Engine;

public class Vehicle {
    private IntegerProperty id;
    private StringProperty plateNumber;
    private FloatProperty weight;
    private FloatProperty height;
    private Engine engine;
    private User user;

    Vehicle() {
        id = new SimpleIntegerProperty();
        plateNumber = new SimpleStringProperty();
        weight = new SimpleFloatProperty();
        height = new SimpleFloatProperty();
        engine = new Engine();
        user = new User();
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

    public String getPlateNumber() {
        return plateNumber.get();
    }

    public StringProperty plateNumberProperty() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber.set(plateNumber);
    }

    public float getWeight() {
        return weight.get();
    }

    public FloatProperty weightProperty() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight.set(weight);
    }

    public float getHeight() {
        return height.get();
    }

    public FloatProperty heightProperty() {
        return height;
    }

    public void setHeight(float height) {
        this.height.set(height);
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
