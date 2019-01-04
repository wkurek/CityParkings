package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.country.Country;

public class UsersTabController {

    @FXML
    public ChoiceBox<Country> usersCountrySelect;
    @FXML
    public DatePicker expirationMinInput;
    @FXML
    public DatePicker expirationMaxInput;
    @FXML
    public TextField vehiclesMaxInput;
    @FXML
    public TextField vehiclesMinInput;
    @FXML
    public ChoiceBox usersColumsSelect;

    @FXML
    public void onUsersFilterClicked() {
    }
}
