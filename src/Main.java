import Utilities.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Customer Scheduling");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        DBConnection.start();
        launch(args);
        DBConnection.close();
        System.out.println("System Exit");
    }


}
