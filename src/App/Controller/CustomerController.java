package App.Controller;

import App.Model.Customer;
import App.Utilities.CustomerDB;
import App.Utilities.Dialog;
import App.Utilities.QueryDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static App.Controller.MainScreenController.*;

public class CustomerController implements Initializable {

    public TextField nameField;
    public TextField phNumberField;
    public TextField address1Field;
    public TextField address2Field;
    public TextField cityField;
    public TextField countryField;
    public TextField zipField;
    private boolean editMode;
    private Customer editCustomer;

    public void onSave(ActionEvent event) {
        if(!editMode) {
            CustomerDB.createCustomer(nameField.getText(), address1Field.getText(), address2Field.getText(), cityField.getText(),
                    countryField.getText(), phNumberField.getText(), zipField.getText());
        }
        else{
            CustomerDB.editCustomer(getCustomerEditID(), editCustomer.getAddress().getAddressID(), nameField.getText(), address1Field.getText(), address2Field.getText(), cityField.getText(),
                    countryField.getText(), phNumberField.getText(), zipField.getText());
        }
        Dialog.dialog("INFORMATION", "Customer "+nameField.getText(), nameField.getText() + " contact information saved.");
        editMode = false;
        resetCustomerEditID();
        returnMain(event);
    }

    public void onCancel(ActionEvent event) {
        Dialog.dialog("CONFIRMATION", "Confirm", "Are you sure you want to cancel?");
        returnMain(event);
    }
    
    private void returnMain(ActionEvent event){
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/App/View/MainScreenView.fxml"));
            stage.setTitle("Customer Schedule");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(getCustomerEditID() >=0){
            editMode = true;
            editCustomer = CustomerDB.searchCustomer(getCustomerEditID());
            nameField.setText(editCustomer.getCustomerName());
            phNumberField.setText(editCustomer.getAddress().getPhone());
            address1Field.setText(editCustomer.getAddress().getAddress());
            address2Field.setText(editCustomer.getAddress().getAddress2());
            cityField.setText(editCustomer.getAddress().getCity().getCityName());
            countryField.setText(editCustomer.getAddress().getCity().getCountry().getCountryName());
            zipField.setText(editCustomer.getAddress().getPostalCode());
        }
    }
}
