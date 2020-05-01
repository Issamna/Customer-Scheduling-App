package App;

import App.Model.User;
import App.Utilities.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class Main extends Application {
    public static User currentUser = new User();

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Locale.setDefault(new Locale("es", "ES"));
        ResourceBundle ln = ResourceBundle.getBundle("App/Utilities/Languages/login");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/View/LoginView.fxml"));
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

}
