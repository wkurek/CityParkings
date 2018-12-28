package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RootController {
    private Stage stage;

    @FXML
    private BorderPane rootContainer;

    @FXML
    private void initialize() {
        //set initial view
        onPreviewUsersClicked();
    }

    @FXML
    public void onNewUserClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserController.class.getResource("../view/newUserView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            NewUserController controller = loader.getController();
            controller.setStage(dialogStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onPreviewUsersClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootController.class.getResource("../view/userView.fxml"));
            AnchorPane anchorPane = loader.load();

            UserController controller = loader.getController();
            controller.setStage(stage);

            rootContainer.setCenter(anchorPane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

