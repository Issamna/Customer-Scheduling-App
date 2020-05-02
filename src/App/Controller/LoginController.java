package App.Controller;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import App.Main;
import App.Model.User;
import App.Utilities.Log;
import App.Utilities.QueryDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import static App.Utilities.Dialog.dialog;

/**
 * Login view controller
 */
public class LoginController implements Initializable {
    @FXML
    public Label loginTitle;
    @FXML
    public Label userNameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    public TextField userField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginBTN;
    @FXML
    public Button cancelBTN;
    private User user = new User();
    private Stage stage;
    private Parent scene;
    ResourceBundle ln;

    /**
     * Method to login
     * @param event
     */
    public void onLogin(ActionEvent event) {
        //get input
        String userInput = userField.getText();
        String passwordInput = passwordField.getText();
        //check for empty field
        if(userInput.length() == 0 || passwordInput.length() == 0){
            dialog("ERROR",ln.getString("errorTitle"), ln.getString("empty"));
        }
        else{
            //validate user
            user = validateUsername(userInput);
            //if user doesn't exist
            if(user == null){
                dialog("ERROR",ln.getString("errorTitle"),ln.getString("incorrectUser"));
            }
            //check if password matches
            else if(user.getPassword().equals(passwordInput)){
                dialog("INFORMATION", ln.getString("successTitle"), ln.getString("successful"));
                //write to loginreport
                Log.writeLog(user.getUserName());
                //make programs current user to user that logged in
                Main.currentUser = user;
                //open main screen
                try {
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/App/View/MainScreenView.fxml"));
                    stage.setTitle("Customer Schedule | Dashboard");
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            else{
                dialog("ERROR",ln.getString("errorTitle"),ln.getString("incorrectPassword"));
            }
        }
    }

    /**
     * Cancel and exit program
     * @param event
     */
    public void onCancel(ActionEvent event) {
        //Exit program
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    /**
     * Validate user
     * @param userInput
     * @return User if exist
     */
    private User validateUsername(String userInput) {
        User userReturn = new User();
        try {
            //Query and get results
            String validateQuery = "SELECT * FROM user WHERE userName = '" + userInput + "'";
            QueryDB.returnQuery(validateQuery);
            ResultSet result = QueryDB.getResult();
            //if exists
            if (result.next()) {
                userReturn.setUserId(result.getInt("userId"));
                userReturn.setUserName(result.getString("userName"));
                userReturn.setPassword(result.getString("password"));
            }
            else {
                return null;
            }
        }catch (SQLException e){
            System.out.println("Error: "+ e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return userReturn;
    }

    /**
     * Initialize login View
     * @param location
     * @param ln
     */
    @Override
    public void initialize(URL location, ResourceBundle ln) {
        this.ln = ln;
        loginTitle.setText(ln.getString("header"));
        userNameLabel.setText(ln.getString("username"));
        passwordLabel.setText(ln.getString("password"));
        loginBTN.setText(ln.getString("login"));
        cancelBTN.setText(ln.getString("cancel"));
    }

}
