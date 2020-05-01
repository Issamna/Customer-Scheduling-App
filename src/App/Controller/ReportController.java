package App.Controller;

import App.Model.*;
import App.Utilities.AppointmentDB;
import App.Utilities.QueryDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static App.Utilities.Dialog.dialog;

public class ReportController implements Initializable {

    public TableView<MonthReport> apptByMonthTable;
    public TableColumn<MonthReport, String> monthCol;
    public TableColumn<MonthReport, String> monthTypeCol;
    public TableColumn<MonthReport, String> monthNumberCol;
    private ObservableList<MonthReport> monthReports;

    public ComboBox<String> consultantsField;
    public TableView<Appointment> apptTable;
    public TableColumn<Appointment, String> apptStartCol;
    public TableColumn<Appointment, String> apptEndCol;
    public TableColumn<Appointment, String> apptTitleCol;
    public TableColumn<Appointment, String> apptTypeCol;
    public TableColumn<Appointment, String> apptCustomerCol;


    public TableView<AverageAppointments> aveApptTable;
    public TableColumn<AverageAppointments, String> todCol;
    public TableColumn<AverageAppointments, String> aveApptCol;

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
    private void apptByMonthTableFill(){
        try {
            monthReports = AppointmentDB.getMonthsReport();
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        apptByMonthTable.setItems(monthReports);
    }

    private void consultantsFieldFill(){
        ObservableList<String> userList = FXCollections.observableArrayList();
        userList.add("All");
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

    private void onUserSelection(){
        ObservableList<Appointment> appointments;
        if(consultantsField.getValue().equals("All")){
            appointments = AppointmentDB.getAllAppointmentsAllUsers();
        }
        else {
            appointments = AppointmentDB.getAppointmentsByUser(consultantsField.getValue());
        }
        apptTable.setItems(appointments);
    }

    private void aveApptTableFill(){
        ObservableList<AverageAppointments> aveAppointments = FXCollections.observableArrayList();
        try {
            aveAppointments = AppointmentDB.getAverageAppointments();
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        aveApptTable.setItems(aveAppointments);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMonth()));
        monthTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        monthNumberCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumber()));
        apptByMonthTableFill();

        consultantsFieldFill();
        apptStartCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        apptEndCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        apptTitleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        apptTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        apptCustomerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustomerName()));
        consultantsField.valueProperty().addListener((ov, t, t1) -> onUserSelection());

        todCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHour()));
        aveApptCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCount()));
        aveApptTableFill();

    }
}
