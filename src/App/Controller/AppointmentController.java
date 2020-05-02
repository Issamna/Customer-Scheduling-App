package App.Controller;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import App.Model.Appointment;
import App.Model.Customer;
import App.Utilities.AppointmentDB;
import App.Utilities.CustomerDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import static App.Controller.MainScreenController.*;
import static App.Utilities.Dialog.confirmationDialog;
import static App.Utilities.Dialog.dialog;

/**
 * Appointment view controller
 */
public class AppointmentController implements Initializable {
    @FXML
    public TableView<Customer> custTable;
    @FXML
    public TableColumn<Customer, String> custNameCol;
    @FXML
    public TableColumn<Customer, String> custPhoneCol;
    @FXML
    public TextField custSearch;
    @FXML
    public TextField titleField;
    @FXML
    public ComboBox<String> typeField;
    @FXML
    public ComboBox<String> locationField;
    @FXML
    public DatePicker dateField;
    @FXML
    public ComboBox<String> startField;
    @FXML
    public ComboBox<String> endField;
    @FXML
    public TextArea descriptionField;
    private ObservableList<Customer> customers;
    private ObservableList<String> startTimeList = FXCollections.observableArrayList();
    private ObservableList<String> endTimeList = FXCollections.observableArrayList();
    private ZonedDateTime startUTC;
    private ZonedDateTime endUTC;
    private DateTimeFormatter timeFormatSrt = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");
    private ZoneId zone = ZoneId.systemDefault();
    private boolean editMode = false;

    /**
     * Save appointment details
     * @param event
     */
    public void onSave(ActionEvent event) {
        boolean error = true;
        //validate fields
        if(validateSaveFields()) {
            try {
                //get customer field. Try/catch for not being selected
                int customerId = custTable.getSelectionModel().getSelectedItem().getCustomerId();
                try {
                    //validate date selection. Try/catch for date not being selected
                    if(validateDate()) {
                        //if saving new appointment
                        if (!editMode) {
                           error = AppointmentDB.createAppointment(customerId, titleField.getText(), startUTC, endUTC, typeField.getValue(), locationField.getValue(), descriptionField.getText());
                        }
                        //if editing new appointment
                        else {
                            error = AppointmentDB.editAppointment(getAppointmentEditID(), customerId, titleField.getText(), startUTC, endUTC, typeField.getValue(), locationField.getValue(), descriptionField.getText());
                        }
                        if(error) {//if true = no error
                            dialog("INFORMATION", "Saved", "Appointment Saved");
                            //reset edit fields
                            editMode = false;
                            resetAppointmentEditID();
                            returnMain(event);
                        }
                    }
                } catch (Exception e) {
                    dialog("ERROR", "Error", "Please choose a date.");
                }
            } catch (Exception e) {
                dialog("ERROR", "Error", "Customer not selected");
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
            resetAppointmentEditID();
            returnMain(event);
        }
    }

    /**
     * Validate date fields and check for overlap
     * @return true if correct
     */
    private boolean validateDate(){
        boolean error = true;
        //Convert from local to UTC
        LocalDateTime startLocal = LocalDateTime.of(dateField.getValue(), LocalTime.parse(startField.getValue(), timeFormatSrt));
        LocalDateTime endLocal = LocalDateTime.of(dateField.getValue(), LocalTime.parse(endField.getValue(), timeFormatSrt));
        startUTC = startLocal.atZone(zone).withZoneSameInstant(ZoneId.of("UTC"));
        endUTC = endLocal.atZone(zone).withZoneSameInstant(ZoneId.of("UTC"));
        //check if end time is before start time
        if(startUTC.equals(endUTC) || startUTC.isAfter(endUTC)){
            error = false;
            dialog("ERROR", "Error", "End time must be at least 15 minutes after start time");
        }
        //check for overlap. If in edit mode, ignore if overlap is this appointmentId
        if(AppointmentDB.validateOverlap(startUTC, endUTC, getAppointmentEditID())){
            dialog("ERROR", "Error", "An appointment already exists in that time frame. Try another time frame");
            error = false;
        }
        return error;
    }

    /**
     * Check if fields are populated (Except description field)
     * @return true if populated
     */
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
        if(typeField.getValue() == null ){
            error++;
            content += "Please choose a type. \n";
        }
        if(locationField.getValue() == null){
            error++;
            content += "Please choose a location. \n";
        }
        if (error == 0){
            return true;
        }
        else {
            dialog("ERROR","Input Error",content);
            return false;
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
     * Search customers in database
     */
    public void onCustSearch() {
        //if search field is empty
        if(custSearch.getText().length() == 0){
            dialog("INFORMATION", "Search Error", "Search field empty");
        }
        else {
            //Query database
            customers = CustomerDB.searchCustomers(custSearch.getText());
            //if list is empty
            if(customers.size()== 0){
                dialog("INFORMATION", "Search Error", "Could not find it");
            }
            else {
                custTable.setItems(customers);
            }
        }
    }

    /**
     * Clear search field and reset table
     */
    public void onCusClear() {
        custSearch.setText("");
        customerTableFill();
    }

    /**
     * Fill customer table
     */
    private void customerTableFill(){
        customers = CustomerDB.getAllCustomers();
        custTable.setItems(customers);
    }

    /**
     * Initialize appointment view
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*Used lambda here to fill table with customer data faster. Used this method in Software 1, seemed the like the
        most convenient and efficient method
        */
        custNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        custPhoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getPhone()));
        customerTableFill();

        //Fill type field
        ObservableList<String> apptType = FXCollections.observableArrayList();
        apptType.addAll( "New", "Follow Up", "Close", "Information", "General");
        typeField.setItems(apptType);
        typeField.setValue("General");

        //Fill location field
        ObservableList<String> apptLocation = FXCollections.observableArrayList();
        apptLocation.addAll( "Reception", "Conference Room", "Lobby", "Office", "External Location");
        locationField.setItems(apptLocation);
        locationField.setValue("Reception");

        //fill start and end times, Restriction from 8AM to 5PM at 15min durations.
        LocalTime time = LocalTime.of(8,0);
        while (!time.equals(LocalTime.of(17, 15))) {
            startTimeList.add(time.format(timeFormatSrt));
            endTimeList.add(time.format(timeFormatSrt));
            time = time.plusMinutes(15);
        }
        startField.setItems(startTimeList);
        endField.setItems(endTimeList);
        startField.getSelectionModel().select(LocalTime.of(8, 0).format(timeFormatSrt));
        endField.getSelectionModel().select(LocalTime.of(8, 0).format(timeFormatSrt));

        //Check if edit mode. Fills fields with data from the main screen selection
        if(getAppointmentEditID() > 0){
            editMode = true;
            Appointment editAppointment = AppointmentDB.searchAppointment(getAppointmentEditID());
            titleField.setText(editAppointment.getTitle());
            //change format of data and time for fields
            LocalDateTime startTime = LocalDateTime.parse(editAppointment.getStart(), df);
            LocalDateTime endTime = LocalDateTime.parse(editAppointment.getEnd(), df);
            LocalDate date = startTime.toLocalDate();
            dateField.setValue(date);
            startField.getSelectionModel().select(startTime.toLocalTime().format(timeFormatSrt));
            endField.getSelectionModel().select(endTime.toLocalTime().format(timeFormatSrt));
            typeField.setValue(editAppointment.getType());
            locationField.setValue(editAppointment.getLocation());
            descriptionField.setText(editAppointment.getDescription());
            //Iterate through customers to find one associated with appointment
            for (Customer customer : customers) {
                if (customer.getCustomerId() == editAppointment.getCustomer().getCustomerId()) {
                    custTable.getSelectionModel().select(customer);
                }
            }
        }
    }
}
