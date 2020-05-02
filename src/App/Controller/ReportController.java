package App.Controller;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import App.Model.*;
import App.Utilities.AppointmentDB;
import App.Utilities.QueryDB;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import static App.Utilities.Dialog.dialog;

/**
 * Report View controller
 */
public class ReportController implements Initializable {

    @FXML
    public TableView<MonthReport> apptByMonthTable;
    @FXML
    public TableColumn<MonthReport, String> monthCol;
    @FXML
    public TableColumn<MonthReport, String> monthTypeCol;
    @FXML
    public TableColumn<MonthReport, String> monthNumberCol;
    @FXML
    private ObservableList<MonthReport> monthReports;
    @FXML
    public ComboBox<String> consultantsField;
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
    public TableView<AverageAppointments> aveApptTable;
    @FXML
    public TableColumn<AverageAppointments, String> todCol;
    @FXML
    public TableColumn<AverageAppointments, String> aveApptCol;

    /**
     * Return to main screen
     * @param event
     */
    public void onShowMain(ActionEvent event) {
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
     * Fill Appointment by Month/type report
     */
    private void apptByMonthTableFill(){
        try {
            //get data from database
            monthReports = AppointmentDB.getMonthsReport();
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        apptByMonthTable.setItems(monthReports);
    }

    /**
     * Fill consults combo box with all users plus a selection for all
     */
    private void consultantsFieldFill(){
        ObservableList<String> userList = FXCollections.observableArrayList();
        userList.add("All");
        //Query and get results
        String query = "SELECT * FROM user";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try {
            while (result.next()) {
                userList.add(result.getString("userName"));
            }
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        consultantsField.setItems(userList);
    }


    /**
     * On user selection in combo box, update table to that user
     */
    private void onUserSelection(){
        ObservableList<Appointment> appointments;
        //if all is selected, return all appointments
        if(consultantsField.getValue().equals("All")){
            appointments = AppointmentDB.getAllAppointmentsAllUsers();
        }
        //else return user selected appointments
        else {
            appointments = AppointmentDB.getAppointmentsByUser(consultantsField.getValue());
        }
        apptTable.setItems(appointments);
    }

    /**
     * Fill Average Appointments table
     */
    private void aveApptTableFill(){
        ObservableList<AverageAppointments> aveAppointments = FXCollections.observableArrayList();
        try {
            //Query and get results
            aveAppointments = AppointmentDB.getAverageAppointments();
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        aveApptTable.setItems(aveAppointments);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*Used lambda here to fill table with data faster. Used this method in Software 1, seemed the like the
        most convenient and efficient method
        */
        //Report 1 fill
        monthCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMonth()));
        monthTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        monthNumberCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumber()));
        apptByMonthTableFill();

        //Report 2 fill
        consultantsFieldFill();
        apptStartCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        apptEndCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        apptTitleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        apptTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        apptCustomerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustomerName()));
        //add listener to consultant combo box
        consultantsField.valueProperty().addListener((ov, t, t1) -> onUserSelection());

        //Report 2 fill
        todCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHour()));
        aveApptCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCount()));
        aveApptTableFill();

    }
}
