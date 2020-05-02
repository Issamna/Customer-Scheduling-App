package App;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import App.Model.User;
import App.Utilities.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Main class to start application
 */
public class Main extends Application {
    //User logged into program
    public static User currentUser = new User();

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Second language chosen is Spanish and country is Spain, uncomment the next line
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
        //create connection
        DBConnection.start();
        launch(args);
        //close connection and exit system
        DBConnection.close();
        System.out.println("System Exit");
    }

}
