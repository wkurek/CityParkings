package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import model.country.Country;
import model.country.CountryDAO;
import model.views.UsersViewDAO;

import java.util.ArrayList;
import java.util.List;


public class UsersTabController {


    @FXML
    public DatePicker expirationMinInput;
    @FXML
    public DatePicker expirationMaxInput;
    @FXML
    public TextField vehiclesMaxInput;
    @FXML
    public TextField vehiclesMinInput;
    @FXML
    public MenuButton countryMenuButton;
    @FXML
    public MenuButton columnMenuButton;

    private List<CheckMenuItem> countryItems;
    private List<CheckMenuItem> columnItems;

    public UsersTabController()
    {
        countryItems = new ArrayList<>();
        columnItems = new ArrayList<>();
    }

    @FXML
    private void initialize()
    {
        columnItems.add(new CheckMenuItem("Select All"));
        for(String i : UsersViewDAO.getColumnsNames()) {
            columnItems.add(new CheckMenuItem(i));
        }
        columnMenuButton.getItems().setAll(columnItems);

        countryItems.add(new CheckMenuItem("Select All"));
        for(Country i : CountryDAO.getCountries()) {
            countryItems.add(new CheckMenuItem(i.toString()));
        }
        countryMenuButton.getItems().setAll(countryItems);

    }
    @FXML
    public void onUsersFilterClicked() {
    }
}
