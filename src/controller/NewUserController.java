package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.address.Address;
import model.address.AddressDAO;
import model.card.CardDAO;
import model.country.Country;
import model.country.CountryDAO;
import model.user.User;
import model.user.UserDAO;
import util.Validator;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class NewUserController {
    private Stage stage;

    @FXML
    private TextField nameInput;
    @FXML
    private TextField surnameInput;
    @FXML
    private TextField phoneNumberInput;
    @FXML
    private TextField cityInput;
    @FXML
    private TextField streetInput;
    @FXML
    private TextField zipCodeInput;
    @FXML
    private TextField numberInput;

    private Task<Void> saveUserTask;

    @FXML
    private ComboBox<Country> countryComboBox;


    @FXML
    private void initialize() {
        countryComboBox.setItems(CountryDAO.getCountries());

        saveUserTask = new Task<Void>() {
            @Override
            protected Void call() throws SQLException {
                model.card.Card card = new model.card.Card();

                    Date date = Date.valueOf(LocalDate.now().plusYears(1));
                    card.setExpirationDate(date);

               CardDAO.saveCard(card);
               Integer cardIndex = CardDAO.getSavedCardIndex();

               Country selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();

               Address address = new Address();
               address.setCity(cityInput.getText());
               address.setZipCode(zipCodeInput.getText());
               address.setStreet(streetInput.getText());
               address.setNumber(numberInput.getText());
               address.setCountry(selectedCountry);


               AddressDAO.saveAddress(address);
               Integer addressIndex = AddressDAO.getSavedAddressIndex();


               if(cardIndex != null && addressIndex != null) {
                   User user = new User();

                   user.setName(nameInput.getText());
                   user.setSurname(surnameInput.getText());
                   user.setPhoneNumber(phoneNumberInput.getText());

                   user.getCard().setCardId(cardIndex);
                   user.getAddress().setId(addressIndex);

                   UserDAO.saveUser(user);

               } else {
                   throw new SQLException("Cannot add new user.");
               }

                return null;
            }
        };

        saveUserTask.setOnFailed(event -> {
            ReportsController.taskAlert(stage, event);
        });
    }

    private void scheduleSaveTask() {
        new Thread(saveUserTask).start();
    }

    private boolean areInputFieldsValid() {
        return Validator.isNameValid(nameInput.getText()) && Validator.isSurnameValid(surnameInput.getText()) &&
                Validator.isPhoneNumberValid(phoneNumberInput.getText()) && Validator.isCityValid(cityInput.getText()) &&
                Validator.isStreetValid(streetInput.getText()) && Validator.isNumberValid(numberInput.getText()) &&
                Validator.isZIPCodeValid(zipCodeInput.getText());
    }

    @FXML
    private void onSaveButtonClicked() {
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

    @FXML
    private void onCancelButtonClicked() {
        if(stage != null) stage.close();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
