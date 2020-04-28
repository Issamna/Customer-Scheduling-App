import Model.User;
import Utilities.DBConnection;
import Utilities.Log;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {
    static User currentUser;
    static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
       // Locale.setDefault(new Locale("es", "ES"));
        ResourceBundle ln = ResourceBundle.getBundle("Utilities/Languages/login");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
        loader.setResources(ln);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle(ln.getString("title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {

        DBConnection.start();
        launch(args);
        DBConnection.close();
        System.out.println("System Exit");
    }
    public void showMainScreen() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Customer Scheduling");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
