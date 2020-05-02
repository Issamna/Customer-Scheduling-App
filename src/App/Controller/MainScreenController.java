package App.Controller;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import App.Main;
import App.Model.Appointment;
import App.Model.Customer;
import App.Utilities.AppointmentDB;
import App.Utilities.CustomerDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.util.ResourceBundle;
import static App.Utilities.Dialog.*;


/**
 * Main View controller
 */
public class MainScreenController implements Initializable {
    @FXML
    public TableView<Customer> custTable;
    @FXML
    public TableColumn<Customer, String> custNameCol;
    @FXML
    public TableColumn<Customer, String> custPhoneCol;
    @FXML
    public TextField custSearch;
    @FXML
    public TableView<Appointment> apptTable;
    @FXML
    public TableColumn<Appointment, String> apptStartCol;
    @FXML
    public TableColumn<Appointment, String> apptEndCol;
    @FXML
    public TableColumn<Appointment, String> apptTitleCol;
    @FXML
    public TableColumn<Appointment, String> apptTypeCol;
    @FXML
    public TableColumn<Appointment, String> apptCustomerCol;
    @FXML
    public RadioButton byWeekRadio;
    @FXML
    public RadioButton byMonthRadio;
    @FXML
    public RadioButton byAllRadio;
    @FXML
    public RadioButton byDayRadio;
    @FXML
    public Label userLabel;
    @FXML
    public Label apptUserLabel;
    private Stage stage;
    private Parent scene;
    private ObservableList<Customer> customers;
    private ObservableList<Appointment> appointments;
    private static int customerEditID = -1;
    private static int appointmentEditID = -1;
    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");

    //Customer related methods
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
     * Create new customer
     * @param event
     */
    public void onCustNew(ActionEvent event) {
        //make sure not in edit mode
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

    /**
     * Delete customer
     */
    public void onCustDelete() {
        try {
            //Select customer to delete
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

    /**
     * Edit customer
     * @param event
     */
    public void onCustEdit(ActionEvent event) {
        try {
            //get customer id to edit, toggles edit in Customer view
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

    /**
     * Get customer id to edit
     * @return customer to edit ID
     */
    static int getCustomerEditID(){
        return customerEditID;
    }

    /**
     * Reset customer id to edit
     */
    static void resetCustomerEditID(){
        customerEditID = -1;
    }
    /**
     * Fill customer table
     */
    private void customerTableFill(){
        customers = CustomerDB.getAllCustomers();
        custTable.setItems(customers);
    }

    //Appointment related methods
    /**
     * Create new appointment
     * @param event
     */
    public void onApptNew(ActionEvent event) {
        //make sure not in edit mode
        resetAppointmentEditID();
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

    /**
     * Delete appointment
     */
    public void onApptDelete() {
        try {
            //Selects appointment to delete
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

    /**
     * Edit appointment
     * @param event
     */
    public void onApptEdit(ActionEvent event) {
        try {
            //get appointment id to edit, toggles edit in Appointment view
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

    /**
     * Get appointment Id to edit
     * @return appointment id
     */
    static int getAppointmentEditID(){
        return appointmentEditID;
    }

    /**
     * Reset appointment id to edit
     */
    static void resetAppointmentEditID(){
        appointmentEditID = -1;
    }

    /**
     * To see all appointments related to user
     */
    public void onApptAll() {
        byMonthRadio.setSelected(false);
        byWeekRadio.setSelected(false);
        byDayRadio.setSelected(false);
        appointmentTableFill();
    }

    /**
     * To see appointment for current day for user
     */
    public void onApptDay() {
        byWeekRadio.setSelected(false);
        byMonthRadio.setSelected(false);
        byAllRadio.setSelected(false);
        //get current local date
        LocalDate now = LocalDate.now();
        FilteredList<Appointment> appointmentFilter = new FilteredList<>(appointments);
        //Use lambda to make it easier to filter through all the data received from the database.
        //Helps not to re-query with the parameters.
        appointmentFilter.setPredicate(row -> {
            LocalDateTime startTime = LocalDateTime.parse(row.getStart(), df);
            LocalDate date = startTime.toLocalDate();
            return (date.equals(now));
        });
        apptTable.setItems(appointmentFilter);
    }

    /**
     * To see appointment for current week for user
     */
    public void onApptWeek() {
        byDayRadio.setSelected(false);
        byMonthRadio.setSelected(false);
        byAllRadio.setSelected(false);
        //get current local date
        LocalDate now = LocalDate.now();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
        //Use lambda to make it easier to filter through all the data received from the database.
        //Helps not to re-query with the parameters.
        filteredData.setPredicate(row -> {
            LocalDateTime startTime = LocalDateTime.parse(row.getStart(), df);
            LocalDate date = startTime.toLocalDate();
            return (date.isAfter(now.minusDays(1)) && date.isBefore(now.plusDays(7)));
        });
        apptTable.setItems(filteredData);
    }

    /**
     * To see appointment for current month for user
     */
    public void onApptMonth() {
        byAllRadio.setSelected(false);
        byDayRadio.setSelected(false);
        byWeekRadio.setSelected(false);
        //get current local date
        LocalDate now = LocalDate.now();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
        //Use lambda to make it easier to filter through all the data received from the database.
        //Helps not to re-query with the parameters.
        filteredData.setPredicate(row -> {
            LocalDateTime startTime = LocalDateTime.parse(row.getStart(), df);
            LocalDate date = startTime.toLocalDate();
            return (date.isAfter(now.minusDays(1)) && date.isBefore(now.plusMonths(1)));
        });
        apptTable.setItems(filteredData);
    }

    /**
     * Fill appointment table
     */
    private void appointmentTableFill(){
        appointments = AppointmentDB.getAllAppointments();
        apptTable.setItems(appointments);
    }

    /**
     * Open report view
     * @param event
     */
    public void onShowReport(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/App/View/ReportView.fxml"));
            stage.setTitle("Customer Schedule | Reports");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Exit program
     * @param event
     */
    public void onExit(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    /**
     * Appointment reminder if one in 15mins
     */
    private void appointmentReminder() {
        //get current local date
        LocalDateTime now = LocalDateTime.now();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
        //Use lambda to make it easier to filter through all the data received from the database.
        //Helps not to re-query with the parameters.
        filteredData.setPredicate(row -> {
            LocalDateTime rowDate = LocalDateTime.parse(row.getStart(), df);
            return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(now.plusMinutes(15));
        });
        //if filtered data is not empty then set reminder
        if (!filteredData.isEmpty()) {
            String customer =  filteredData.get(0).getCustomer().getCustomerName();
            String start = filteredData.get(0).getStart();
            String content = "You have a meeting with "+customer+" at "+start+".";
            dialog("INFORMATION", "Meeting Reminder", content);
        }
    }

    /**
     * Initialize main view
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set labels to current users
        userLabel.setText(Main.currentUser.getUserName());
        apptUserLabel.setText(Main.currentUser.getUserName());

        /*Used lambda here to fill table with customer data faster. Used this method in Software 1, seemed the like the
        most convenient and efficient method
        */
        custNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        custPhoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getPhone()));
        customerTableFill();

        /*Used lambda here to fill table with appointment data faster. Used this method in Software 1, seemed the like the
        most convenient and efficient method
        */
        apptStartCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        apptEndCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        apptTitleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        apptTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        apptCustomerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustomerName()));
        appointmentTableFill();
        byDayRadio.setSelected(true);
        onApptDay();

        //check for appointments
        appointmentReminder();
    }
}
