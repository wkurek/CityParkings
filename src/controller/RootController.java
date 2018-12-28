package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class RootController {

    @FXML
    private BorderPane rootContainer;

    @FXML
    private void initialize() {
        //set initial view
        onPreviewUsersClicked();
    }

    @FXML
    public void onNewUserClicked() {

    }

    @FXML
    public void onPreviewUsersClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootController.class.getResource("../view/userView.fxml"));
            AnchorPane anchorPane = loader.load();

            rootContainer.setCenter(anchorPane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

