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

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
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
    private User user = new User();

    public void onLogin(ActionEvent event) {
        //get input
        String userInput = userField.getText();
        String passwordInput = passwordField.getText();
        //check for empty field
        if(userInput.length() == 0 || passwordInput.length() == 0){
            dialog("ERROR","Login Error", "Login fields are empty");
        }
        else{
            user = validateUsername(userInput);
            if(user == null){
                dialog("ERROR","Login Error", "Username does not exist");
            }
            else if(user.getPassword().equals(passwordInput)){
                dialog("INFORMATION", "Login", "Login Successful");
            }
            else{
                dialog("ERROR","Login Error","Password does not match");
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
            } else {
                return null;
            }

        }catch (SQLException e){
            System.out.println("Error: "+ e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return userReturn;
    }

}
