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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    public RadioButton byWeekRadio;
    public RadioButton byMonthRadio;
    public RadioButton byAllRadio;
    public RadioButton byDayRadio;
    private Stage stage;
    private Parent scene;
    private ObservableList<Customer> customers;
    private ObservableList<Appointment> appointments;
    private static int customerEditID = -1;
    private static int appointmentEditID = -1;
    private DateTimeFormatter timeFormatSrt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");

    public void onCustSearch() {
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

    public void onCusClear() {
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
    public void onCustDelete() {
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

    public void onApptSearch() {
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
    public void onApptClear() {
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

    public void onApptAll() {
        byMonthRadio.setSelected(false);
        byWeekRadio.setSelected(false);
        byDayRadio.setSelected(false);
        appointmentTableFill();
    }

    public void onApptDay() {
        byWeekRadio.setSelected(false);
        byMonthRadio.setSelected(false);
        byAllRadio.setSelected(false);

        LocalDate now = LocalDate.now();
        FilteredList<Appointment> appointmentFilter = new FilteredList<>(appointments);
        appointmentFilter.setPredicate(row -> {
            LocalDateTime startTime = LocalDateTime.parse(row.getStart(), df);
            LocalDate date = startTime.toLocalDate();
            return (date.equals(now));
        });
        apptTable.setItems(appointmentFilter);
    }

    public void onApptWeek() {
        byDayRadio.setSelected(false);
        byMonthRadio.setSelected(false);
        byAllRadio.setSelected(false);

        LocalDate now = LocalDate.now();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
        filteredData.setPredicate(row -> {
            LocalDateTime startTime = LocalDateTime.parse(row.getStart(), df);
            LocalDate date = startTime.toLocalDate();
            return (date.isAfter(now.minusDays(1)) && date.isBefore(now.plusDays(7)));
        });
        apptTable.setItems(filteredData);
    }

    public void onApptMonth() {
        byAllRadio.setSelected(false);
        byAllRadio.setSelected(false);
        byWeekRadio.setSelected(false);
        LocalDate now = LocalDate.now();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
        filteredData.setPredicate(row -> {
            LocalDateTime startTime = LocalDateTime.parse(row.getStart(), df);
            LocalDate date = startTime.toLocalDate();
            return (date.isAfter(now.minusDays(1)) && date.isBefore(now.plusMonths(1)));
        });
        apptTable.setItems(filteredData);
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
        byDayRadio.setSelected(true);
        onApptDay();
    }
}
