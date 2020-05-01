package App.Controller;
/*
C195 Performance Assessment
Issam Ahmed
000846138
4/27/2020
*/


import App.Model.Appointment;
import App.Model.Customer;
import App.Utilities.AppointmentDB;
import App.Utilities.CustomerDB;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static App.Utilities.Dialog.*;


public class MainScreenController implements Initializable {
    public TableView<Customer> custTable;
    public TableColumn<Customer, String> custNameCol;
    public TableColumn<Customer, String> custPhoneCol;
    public TextField custSearch;
    public TableView<Appointment> apptTable;
    public TableColumn<Appointment, String> apptStartCol;
    public TableColumn<Appointment, String> apptEndCol;
    public TableColumn<Appointment, String> apptTitleCol;
    public TableColumn<Appointment, String> apptTypeCol;
    public TableColumn<Appointment, String> apptCustomerCol;
    public TextField apptSearch;
    private Stage stage;
    private Parent scene;
    private ObservableList<Customer> customers;
    private ObservableList<Appointment> appointments;
    private static int customerEditID = -1;
    private static int appointmentEditID = -1;

    public void onCustSearch(ActionEvent event) {
        if(custSearch.getText().length() == 0){
            dialog("INFORMATION", "Search Error", "Search field empty");
        }
        else {
            customers = CustomerDB.searchCustomers(custSearch.getText());
            if(customers.size()== 0){
                dialog("INFORMATION", "Search Error", "Could not find it");
            }
            else {
                custTable.setItems(customers);
            }
        }
    }

    public void onCusClear(ActionEvent event) {
        custSearch.setText("");
        customerTableFill();

    }

    public void onCustNew(ActionEvent event) {
        resetCustomerEditID();
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/App/View/CustomerView.fxml"));
            stage.setTitle("Customer Schedule | Add New Customer");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    static int getCustomerEditID(){
        return customerEditID;
    }
    static void resetCustomerEditID(){
        customerEditID = -1;
    }
    public void onCustEdit(ActionEvent event) {
        try {
            customerEditID = custTable.getSelectionModel().getSelectedItem().getCustomerId();
            try {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/App/View/CustomerView.fxml"));
                stage.setTitle("Customer Schedule | Edit Customer");
                stage.setScene(new Scene(scene));
                stage.show();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        catch(Exception e){
            dialog("INFORMATION", "Error", "Nothing selected to delete");
        }
    }
    public void onCustDelete(ActionEvent event) {
        try {
            Customer customerDelete = custTable.getSelectionModel().getSelectedItem();
            String content = "Are you sure you want to delete "+customerDelete.getCustomerName()+"? It will delete all appointments related to this customer";
            if(confirmationDialog("Delete Customer?", content)){
                CustomerDB.deleteCustomer(customerDelete);
                customerTableFill();
                appointmentTableFill();
            }
        }
        catch(Exception e){
            dialog("INFORMATION", "Error", "Nothing selected to delete");
        }
    }

    public void onApptSearch(ActionEvent event) {
        if(apptSearch.getText().length() == 0){
            dialog("INFORMATION", "Search Error", "Search field empty");
        }
        else {
            appointments = AppointmentDB.searchAppointment(apptSearch.getText());
            if(appointments.size()== 0){
                dialog("INFORMATION", "Search Error", "Could not find it");
            }
            else {
                apptTable.setItems(appointments);
            }
        }
    }

    public void onApptClear(ActionEvent event) {
        apptSearch.setText("");
        appointmentTableFill();
    }

    public void onApptNew(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/App/View/AppointmentView.fxml"));
            stage.setTitle("Customer Schedule | Add New Appointment");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    static int getAppointmentEditID(){
        return appointmentEditID;
    }
    static void resetAppointmentEditID(){
        appointmentEditID = -1;
    }
    public void onApptEdit(ActionEvent event) {
        try {
            appointmentEditID = apptTable.getSelectionModel().getSelectedItem().getAppointmentId();
            try {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/App/View/AppointmentView.fxml"));
                stage.setTitle("Customer Schedule | Edit Appointment");
                stage.setScene(new Scene(scene));
                stage.show();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        catch(Exception e){
            dialog("INFORMATION", "Error", "Nothing selected to edit");
        }
    }

    public void onApptDelete(ActionEvent event) {
        try {
            Appointment appointmentDelete = apptTable.getSelectionModel().getSelectedItem();
            String content = "Are you sure you want to delete "+appointmentDelete.getTitle()+"?";
            if(confirmationDialog("Delete appointment?", content)){
                AppointmentDB.deleteAppointment(appointmentDelete);
                appointmentTableFill();
            }
        }
        catch(Exception e){
            dialog("INFORMATION", "Error", "Nothing selected to delete");
        }
    }


    public void onShowReport(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/App/View/ReportView.fxml"));
            stage.setTitle("Customer Schedule | Add New Report");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void onApptAll(ActionEvent event) {
    }
    public void onApptWeek(ActionEvent event) {
    }

    public void onApptMonth(ActionEvent event) {
    }


    public void onExit(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    private void customerTableFill(){
        customers = CustomerDB.getAllCustomers();
        custTable.setItems(customers);

    }
    private void appointmentTableFill(){
        appointments = AppointmentDB.getAllAppointments();
        apptTable.setItems(appointments);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        custNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        custPhoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getPhone()));
        customerTableFill();

        apptStartCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        apptEndCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        apptTitleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        apptTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        apptCustomerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustomerName()));
        appointmentTableFill();
    }
}
