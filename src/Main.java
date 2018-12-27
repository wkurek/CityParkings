import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.employee.Employee;
import model.employee.EmployeeDAO;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/rootLayout.fxml"));
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        ObservableList<Employee> list = EmployeeDAO.getEmployees();

        launch(args);
    }
}
