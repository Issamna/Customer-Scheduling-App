package App.Controller;

import App.Model.Appointment;
import App.Model.Customer;
import App.Utilities.AppointmentDB;
import App.Utilities.CustomerDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import static App.Controller.MainScreenController.*;

import static App.Utilities.Dialog.dialog;

public class AppointmentController implements Initializable {
    public TableView<Customer> custTable;
    public TableColumn<Customer, String> custNameCol;
    public TableColumn<Customer, String> custPhoneCol;
    public TextField custSearch;
    public TextField titleField;
    public TextField typeField;
    public TextField locationField;
    public DatePicker dateField;
    public ComboBox<String> startField;
    public ComboBox<String> endField;
    public TextArea descriptionField;
    private ObservableList<Customer> customers;
    private ObservableList<String> startTimeList = FXCollections.observableArrayList();
    private ObservableList<String> endTimeList = FXCollections.observableArrayList();
    private DateTimeFormatter timeFormatSrt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");
    private ZoneId zone = ZoneId.systemDefault();
    private boolean editMode = false;
    private Appointment editAppointment;
    private ZonedDateTime startUTC;
    private ZonedDateTime endUTC;
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

    public void onSave(ActionEvent event) {
        if(validateSaveFields()) {
            try {
                int customerId = custTable.getSelectionModel().getSelectedItem().getCustomerId();
                try {
                    if(validateDate()) {
                        if (!editMode) {
                            AppointmentDB.createAppointment(customerId, titleField.getText(), startUTC, endUTC, typeField.getText(), locationField.getText(), descriptionField.getText());
                        } else {
                            AppointmentDB.editAppointment(getAppointmentEditID(), customerId, titleField.getText(), startUTC, endUTC, typeField.getText(), locationField.getText(), descriptionField.getText());
                        }
                        dialog("INFORMATION", "Saved", "Appointment Saved");
                        editMode = false;
                        resetAppointmentEditID();
                        returnMain(event);
                    }
                } catch (Exception e) {
                    dialog("ERROR", "Error", "Please choose a date.");
                }
            } catch (Exception e) {
                dialog("ERROR", "Error", "Customer not selected");
            }
        }
    }
    private boolean validateDate(){
        boolean error = true;
        LocalDateTime startLocal = LocalDateTime.of(dateField.getValue(), LocalTime.parse(startField.getValue(), timeFormatSrt));
        LocalDateTime endLocal = LocalDateTime.of(dateField.getValue(), LocalTime.parse(endField.getValue(), timeFormatSrt));
        startUTC = startLocal.atZone(zone).withZoneSameInstant(ZoneId.of("UTC"));
        endUTC = endLocal.atZone(zone).withZoneSameInstant(ZoneId.of("UTC"));
        if(startUTC.equals(endUTC) || startUTC.isAfter(endUTC)){
            error = false;
            dialog("ERROR", "Error", "End time must be at least 15 minutes after start time");
        }
        if(AppointmentDB.validateOverlap(startUTC, endUTC, getAppointmentEditID())){
            dialog("ERROR", "Error", "An appointment already exists in that time frame. Try another time frame");
            error = false;
        }

        return error;
    }

    public void onCancel(ActionEvent event) {
        dialog("CONFIRMATION", "Confirm", "Are you sure you want to cancel?");
        returnMain(event);
    }

    private boolean validateSaveFields(){
        int error = 0;
        String content = "";
        if(titleField.getText().length() == 0 ){
            error++;
            content += "Please enter a title. \n";
        }
        if(dateField.getValue() == null ){
            error++;
            content += "Please choose a date. \n";
        }
        if(typeField.getText().length() == 0 ){
            error++;
            content += "Please enter a type. \n";
        }
        if(locationField.getText().length() == 0 ){
            error++;
            content += "Please enter a location. \n";
        }
        if (error == 0){
            return true;
        }
        else {
            dialog("ERROR","Input Error",content);
            return false;
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

    private void customerTableFill(){
            customers = CustomerDB.getAllCustomers();
            custTable.setItems(customers);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        custNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        custPhoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getPhone()));
        customerTableFill();

        LocalTime time = LocalTime.of(8,0);
        while (!time.equals(LocalTime.of(17, 15))) {
            startTimeList.add(time.format(timeFormatSrt));
            endTimeList.add(time.format(timeFormatSrt));
            time = time.plusMinutes(15);
        }
        startField.setItems(startTimeList);
        endField.setItems(endTimeList);
        startField.getSelectionModel().select(LocalTime.of(8, 0).format(timeFormatSrt));
        endField.getSelectionModel().select(LocalTime.of(8, 15).format(timeFormatSrt));

        if(getAppointmentEditID() >= 0){
            editMode = true;
            editAppointment = AppointmentDB.searchAppointment(getAppointmentEditID());
            titleField.setText(editAppointment.getTitle());
            LocalDateTime startTime = LocalDateTime.parse(editAppointment.getStart(), df);
            LocalDateTime endTime = LocalDateTime.parse(editAppointment.getEnd(), df);
            LocalDate date = startTime.toLocalDate();
            dateField.setValue(date);
            startField.getSelectionModel().select(startTime.toLocalTime().format(timeFormatSrt));
            endField.getSelectionModel().select(endTime.toLocalTime().format(timeFormatSrt));
            typeField.setText(editAppointment.getType());
            locationField.setText(editAppointment.getLocation());
            descriptionField.setText(editAppointment.getDescription());
            Customer customer = editAppointment.getCustomer();
            System.out.println(customer.getCustomerName());
            for(int i = 0; i < customers.size(); i++) {
                if(customers.get(i).getCustomerId() == editAppointment.getCustomer().getCustomerId()) { // we found a match
                    custTable.getSelectionModel().select(customers.get(i));
                }
            }
        }


    }
}
