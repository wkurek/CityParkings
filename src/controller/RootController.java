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

    @FXML
    public void onNewParkingClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootController.class.getResource("../view/newParkingView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Parking");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            NewParkingController controller = loader.getController();
            controller.setStage(dialogStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onPreviewParkingsClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootController.class.getResource("../view/parkingView.fxml"));
            AnchorPane anchorPane = loader.load();

            rootContainer.setCenter(anchorPane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void onNewParkClicked() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ParkController.class.getResource("../view/newParkView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Park");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            NewParkController controller = loader.getController();
            controller.setStage(dialogStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onPreviewParksClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootController.class.getResource("../view/parksView.fxml"));
            AnchorPane anchorPane = loader.load();

            ParkController controller = loader.getController();
            controller.setStage(stage);
            rootContainer.setCenter(anchorPane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void onNewEmployeeClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UserController.class.getResource("../view/newEmployeeView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New Employee");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            NewEmployeeController controller = loader.getController();
            controller.setStage(dialogStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onPreviewEmployeesClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootController.class.getResource("../view/employeeView.fxml"));
            AnchorPane anchorPane = loader.load();

            EmployeeController controller = loader.getController();
            controller.setStage(stage);

            rootContainer.setCenter(anchorPane);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void onReportsClicked()
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RootController.class.getResource("../view/reportsView.fxml"));
            AnchorPane anchorPane = loader.load();

            ReportsController controller = loader.getController();
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

