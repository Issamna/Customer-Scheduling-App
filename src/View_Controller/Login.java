package View_Controller;
/*
C195 Performance Assessment
Issam Ahmed
000846138
4/27/2020
*/
import Model.User;
import static Utilities.Dialog.dialog;

import Utilities.DBConnection;
import Utilities.QueryDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Login implements Initializable {
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
    ResourceBundle ln;

    public void onLogin(ActionEvent event) {
        //get input
        String userInput = userField.getText();
        String passwordInput = passwordField.getText();
        //check for empty field
        if(userInput.length() == 0 || passwordInput.length() == 0){
            dialog("ERROR",ln.getString("errorTitle"), ln.getString("empty"));
        }
        else{
            user = validateUsername(userInput);
            if(user == null){
                dialog("ERROR",ln.getString("errorTitle"),ln.getString("incorrectUser"));
            }
            else if(user.getPassword().equals(passwordInput)){
                dialog("INFORMATION", ln.getString("successTitle"), ln.getString("successful"));
            }
            else{
                dialog("ERROR",ln.getString("errorTitle"),ln.getString("incorrectPassword"));
            }
        }
    }

    public void onCancel(ActionEvent event) {
        DBConnection.close();
        System.exit(0);
    }

    private User validateUsername(String userInput) {
        User userReturn = new User();
        try {
            String validateQuery = "SELECT * FROM user WHERE userName = '" + userInput + "'";
            QueryDB.selectQuery(validateQuery);
            ResultSet result = QueryDB.getResult();
            if (result.next()) {
                userReturn.setUserID(result.getInt("userId"));
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
