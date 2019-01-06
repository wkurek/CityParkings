import controller.NewUserController;
import controller.RootController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.department.Department;
import model.department.DepartmentDAO;
import model.park.Park;
import model.park.ParkDAO;
import model.parking.Parking;
import model.parking.ParkingDAO;
import model.vehicle.Vehicle;
import model.vehicle.VehicleDAO;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Application {
    private static final String WINDOW_TITLE = "City parking";

    private AnchorPane rootView;

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle(WINDOW_TITLE);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/rootView.fxml"));
            rootView = loader.load();

            RootController controller = loader.getController();
            controller.setStage(primaryStage);

            primaryStage.setScene(new Scene(rootView));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRootView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/rootView.fxml"));
            rootView = loader.load();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public static void main(String[] args) {

        launch(args);
    }
}
