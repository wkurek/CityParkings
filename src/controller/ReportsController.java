package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ReportsController {

    private Stage stage;






    public ReportsController()
    {



        //employeesCountryChoices =
    }

    @FXML
    private void initialize()
    {

    }







//
//
//    private Task<ObservableList<User>> generateUsersLoadTask() {
//        Task<ObservableList<User>> task = new Task<ObservableList<User>>() {
//            @Override
//            protected ObservableList<User> call() {
//                return UserDAO.getUsers();
//            }
//        };
//
//        task.setOnSucceeded(event -> {
//            usersList.clear();
//            usersList.addAll(task.getValue());
//        });
//
//        task.setOnFailed(event -> {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.initOwner(stage);
//            alert.setTitle("SQL Error");
//            alert.setHeaderText(event.getSource().getException().getMessage());
//            alert.show();
//        });
//
//        return task;
//    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
}