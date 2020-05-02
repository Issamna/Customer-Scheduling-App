package App.Controller;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import App.Model.Customer;
import App.Utilities.CustomerDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

/**
 * Customer View controller
 */
public class CustomerController implements Initializable {
    @FXML
    public TextField nameField;
    @FXML
    public TextField phNumberField;
    @FXML
    public TextField address1Field;
    @FXML
    public TextField address2Field;
    @FXML
    public TextField cityField;
    @FXML
    public TextField countryField;
    @FXML
    public TextField postalCodeField;
    private boolean editMode;
    private Customer editCustomer;

    /**
     * Save customer details
     * @param event
     */
    public void onSave(ActionEvent event) {
        boolean error = true;
        //validate fields
        if(validateSave()) {
            //if saving new customer
            if (!editMode) {
                error = CustomerDB.createCustomer(nameField.getText(), address1Field.getText(), address2Field.getText(), cityField.getText(),
                        countryField.getText(), postalCodeField.getText(), phNumberField.getText());
            }
            //if editing new customer
            else {
                error = CustomerDB.editCustomer(getCustomerEditID(), editCustomer.getAddress().getAddressId(), nameField.getText(), address1Field.getText(), address2Field.getText(), cityField.getText(),
                        countryField.getText(), phNumberField.getText(), postalCodeField.getText());
            }
            if(error) {//if true = no error
                dialog("INFORMATION", "Customer " + nameField.getText(), nameField.getText() + " contact information saved.");
                //reset edit fields
                editMode = false;
                resetCustomerEditID();
                returnMain(event);
            }
        }
    }

    /**
     * Cancel and return to main screen
     * @param event
     */
    public void onCancel(ActionEvent event) {
        //User confirmation to cancel
        if(confirmationDialog("Cancel", "Are you sure you want to cancel?")){
            //reset edit fields
            editMode = false;
            resetCustomerEditID();
            returnMain(event);
        }
    }

    /**
     * Method to return to main screen
     * @param event
     */
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

    /**
     * Check if fields are populated (Except Address 2 Field)
     * @return true if populated
     */
    private boolean validateSave(){
        int error = 0;
        String content = "";
        if(nameField.getText().length() == 0 ){
            error++;
            content += "Please enter a name. \n";
        }
        if(phNumberField.getText().length() <10 || phNumberField.getText().length()>20){
            error++;
            content += "Please enter valid phone number. (10 digits and less then 20 digits)\n";
        }
        else if(isNotInteger(phNumberField.getText())){
            error++;
            content += "Please enter valid phone number. No text or dashes. \n";
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
            content += "Please enter valid ZIP/Postal Code. (5 digits and less then 10 digits)\n";
        }
        else if(isNotInteger(postalCodeField.getText())){
            error++;
            content += "Please enter valid ZIP/Postal Code. No text or dashes. \n";
        }
        if (error == 0){
            return true;
        }
        else {
            dialog("ERROR","Input Error",content);
            return false;
        }
    }
    //check if integer for phone number and zip/postal code
    private boolean isNotInteger(String input){
        try {
            Integer.parseInt(input);
            return false;
        }
        catch (Exception e) {
            return true;
        }
    }

    /**
     * Initialize customer view
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Check if edit mode. Fills fields with data from the main screen selection
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
