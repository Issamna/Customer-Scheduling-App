package App.Controller;

import App.Model.Customer;
import App.Utilities.CustomerDB;
import App.Utilities.Dialog;
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
import static App.Utilities.Dialog.confirmationDialog;
import static App.Utilities.Dialog.dialog;

public class CustomerController implements Initializable {

    public TextField nameField;
    public TextField phNumberField;
    public TextField address1Field;
    public TextField address2Field;
    public TextField cityField;
    public TextField countryField;
    public TextField postalCodeField;
    private boolean editMode;
    private Customer editCustomer;

    public void onSave(ActionEvent event) {
        if(validateSave()) {
            if (!editMode) {
                CustomerDB.createCustomer(nameField.getText(), address1Field.getText(), address2Field.getText(), cityField.getText(),
                        countryField.getText(), postalCodeField.getText(), phNumberField.getText());
            } else {
                CustomerDB.editCustomer(getCustomerEditID(), editCustomer.getAddress().getAddressID(), nameField.getText(), address1Field.getText(), address2Field.getText(), cityField.getText(),
                        countryField.getText(), phNumberField.getText(), postalCodeField.getText());
            }
            Dialog.dialog("INFORMATION", "Customer " + nameField.getText(), nameField.getText() + " contact information saved.");
            editMode = false;
            resetCustomerEditID();
            returnMain(event);
        }
    }

    public void onCancel(ActionEvent event) {
        if(confirmationDialog("Cancel", "Are you sure you want to cancel?")){
            editMode = false;
            resetCustomerEditID();
            returnMain(event);
        }
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

    private boolean validateSave(){
        int error = 0;
        String content = "";
        if(nameField.getText().length() == 0 ){
            error++;
            content += "Please enter a name. \n";
        }
        if(phNumberField.getText().length() <10 || phNumberField.getText().length()>20){
            error++;
            content += "Please enter valid phone number. \n";
        }
        if(address1Field.getText().length() == 0 ){
            error++;
            content += "Please enter an address. \n";
        }
        if(cityField.getText().length() == 0 ){
            error++;
            content += "Please enter a city. \n";
        }
        if(countryField.getText().length() == 0 ){
            error++;
            content += "Please enter a country. \n";
        }
        if(postalCodeField.getText().length() <5 || postalCodeField.getText().length()>10){
            error++;
            content += "Please enter valid ZIP/Postal Code. \n";
        }
        if (error == 0){
            return true;
        }
        else {
            dialog("ERROR","Input Error",content);
            return false;
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
            postalCodeField.setText(editCustomer.getAddress().getPostalCode());
        }
    }
}
