import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static final String WINDOW_TITLE = "City parking";

    private AnchorPane rootView;

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle(WINDOW_TITLE);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/parksView.fxml"));
            rootView = loader.load();

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
