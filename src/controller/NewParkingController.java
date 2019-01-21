package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.city_parking.CityParking;
import model.city_parking.CityParkingDAO;
import model.estate.Estate;
import model.estate.EstateDAO;
import model.estate_parking.EstateParking;
import model.estate_parking.EstateParkingDAO;
import model.kiss_ride.KissRide;
import model.kiss_ride.KissRideDAO;
import model.location.Location;
import model.location.LocationDAO;
import model.park_ride.ParkRide;
import model.park_ride.ParkRideDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import util.Validator;


import java.sql.SQLException;

public class NewParkingController {

    @FXML
    public Pane cityPane;
    @FXML
    public Pane parkRidePane;
    @FXML
    public Pane kissRidePane;
    @FXML
    public Text estateNameLabel;
    private Stage stage;
    @FXML
    public TextField nodeInput;
    @FXML
    public TextField parkTypeInput;
    @FXML
    public TextField nrOfStandardLotsInput;
    @FXML
    public TextField nrOfDisabledLotsInput;
    @FXML
    public TextField maxWeightInput;
    @FXML
    public TextField maxHeightInput;
    @FXML
    public DatePicker lastControlInput;
    @FXML
    public TextField latInput;
    @FXML
    public TextField longInput;
    @FXML
    public CheckBox roofedCheckBox;
    @FXML
    public CheckBox guardedCheckBox;
    @FXML
    public RadioButton estateRadioButton;
    @FXML
    public ComboBox estateComboBox;
    @FXML
    public RadioButton cityRadioButton;
    @FXML
    public CheckBox inPaidCheckBox;
    @FXML
    public RadioButton parkRideRadioButton;
    @FXML
    public CheckBox automaticCheckBox;
    @FXML
    public RadioButton kissRideRadioButton;
    @FXML
    public TextField maxStopInput;
    @FXML
    public CheckBox gatesCheckBox;

    private ToggleGroup radioButtons;

    private Task<Void> saveParkingTask;

    @FXML
    private void initialize() {
        estateComboBox.setItems(EstateDAO.getEstate());

        estateComboBox.setVisible(false);
        cityPane.setVisible(false);
        parkRidePane.setVisible(false);
        kissRidePane.setVisible(false);

        radioButtons = new ToggleGroup();
        estateRadioButton.setToggleGroup(radioButtons);
        cityRadioButton.setToggleGroup(radioButtons);
        parkRideRadioButton.setToggleGroup(radioButtons);
        kissRideRadioButton.setToggleGroup(radioButtons);



        estateRadioButton.selectedProperty().addListener(new RadioButtounsListener());
        cityRadioButton.selectedProperty().addListener(new RadioButtounsListener());
        parkRideRadioButton.selectedProperty().addListener(new RadioButtounsListener());
        kissRideRadioButton.selectedProperty().addListener(new RadioButtounsListener());

        cityRadioButton.setSelected(true);

        lastControlInput.getEditor().setDisable(true);

        saveParkingTask = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                Location location = new Location();

                location.setLatitude(Float.parseFloat(latInput.getText()));
                location.setLongitude(Float.parseFloat(longInput.getText()));

                LocationDAO.saveLocation(location);
                location.setLocationId(LocationDAO.getSavedLocationId());

                Parking newParking = new Parking();
                newParking.setStandardLots(Integer.parseInt(nrOfStandardLotsInput.getText()));
                newParking.setDisabledLots(Integer.parseInt(nrOfDisabledLotsInput.getText()));
                newParking.setGuarded(guardedCheckBox.isSelected());
                newParking.setRoofed(roofedCheckBox.isSelected());
                newParking.setLastControl(java.sql.Date.valueOf(lastControlInput.getValue()));
                newParking.setLocation(location);
                newParking.setMaxHeight(Float.parseFloat(maxHeightInput.getText()));
                newParking.setMaxWeight(Float.parseFloat(maxWeightInput.getText()));

                ParkingDAO.saveParking(newParking);
                newParking.setParkingId(ParkingDAO.getSavedParkingId());

                switch (((RadioButton) radioButtons.getSelectedToggle()).getText()) {
                    case ("Estate"): {
                        EstateParking newEstateParking = new EstateParking();
                        newEstateParking.setEstate(EstateDAO.getEstate(((Estate) estateComboBox.getValue()).getEstateName()));
                        newEstateParking.setParking(newParking);
                        EstateParkingDAO.saveEstateParking(newEstateParking);

                    }
                    case ("City"): {
                        CityParking newCityParking = new CityParking();
                        newCityParking.setParkType(parkTypeInput.getText());
                        newCityParking.setParking(newParking);
                        newCityParking.setZonePaid(inPaidCheckBox.isSelected());
                        CityParkingDAO.saveCityParking(newCityParking);
                    }
                    case ("Park&Ride"): {
                        ParkRide newParkRide = new ParkRide();
                        newParkRide.setAutomatic(automaticCheckBox.isSelected());
                        newParkRide.setCommunicationNode(nodeInput.getText());
                        newParkRide.setParking(newParking);
                        ParkRideDAO.saveParkRide(newParkRide);
                    }
                    case ("Kiss&Ride"): {
                        KissRide newKissRide = new KissRide();
                        newKissRide.setAreGates(gatesCheckBox.isSelected());
                        newKissRide.setMaxStopMinutes(Integer.parseInt(maxStopInput.getText()));
                        newKissRide.setParking(newParking);
                        KissRideDAO.saveKissRide(newKissRide);
                    }
                    default:{}
                }

                return null;
            }
        };
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onSaveButtonClicked() {
            if(areInputFieldsValid()) {
                scheduleSaveTask();
                onCancelButtonClicked();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(stage);
                alert.setTitle("Invalid field(s)");
                alert.show();
            }
        }

    private void scheduleSaveTask() {
        new Thread(saveParkingTask).start();
    }

    private boolean areInputFieldsValid() {
        boolean isOK = Validator.isIntegerInputValid(nrOfStandardLotsInput.getText()) && Validator.isIntegerInputValid(nrOfDisabledLotsInput.getText())
                && Validator.isWeightValid(maxWeightInput.getText()) && Validator.isHeightValid(maxHeightInput.getText())
                && Validator.isLatitudeValid(latInput.getText()) && Validator.isLongitudeValid(longInput.getText());
        switch (((RadioButton)radioButtons.getSelectedToggle()).getText())
        {
            case("Estate"):
            {
                return isOK;
            }
            case("City"):
            {
                isOK = isOK && Validator.isCityValid(parkTypeInput.getText());
                return isOK;
            }
            case("Park&Ride"):
            {
                isOK = isOK && Validator.isCityValid(nodeInput.getText());
                return isOK;
            }
            case("Kiss&Ride"):
            {
                isOK = isOK && Validator.isIntegerInputValid(maxStopInput.getText());
                return isOK;
            }
            default: return false;
        }


    }

    private class RadioButtounsListener implements ChangeListener<Boolean>
    {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                estateComboBox.setVisible(false);
                estateNameLabel.setVisible(false);
                cityPane.setVisible(false);
                parkRidePane.setVisible(false);
                kissRidePane.setVisible(false);

            switch (((RadioButton)radioButtons.getSelectedToggle()).getText())
            {
                case("Estate"):
                {
                    estateComboBox.setVisible(true);
                    estateNameLabel.setVisible(true);
                    break;
                }
                case("City"):
                {
                   cityPane.setVisible(true);
                   break;
                }
                case("Park&Ride"):
                {
                    parkRidePane.setVisible(true);
                    break;
                }
                case("Kiss&Ride"):
                {
                    kissRidePane.setVisible(true);
                    break;
                }
                default: {}
            }

        }
    }

    public void onCancelButtonClicked() {
        if(stage != null) stage.close();
    }
}
