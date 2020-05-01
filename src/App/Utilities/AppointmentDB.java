package App.Utilities;

import App.Main;
import App.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static App.Main.currentUser;
import static App.Utilities.Dialog.dialog;

public class AppointmentDB {
    private static ZoneId zone = ZoneId.systemDefault();
    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");

    public static void createAppointment(int customerID, String title, ZonedDateTime start, ZonedDateTime end, String type, String location, String description){
        Timestamp startTimeStamp = Timestamp.valueOf(start.toLocalDateTime());
        Timestamp endTimeStamp = Timestamp.valueOf(end.toLocalDateTime());
        String query = "INSERT INTO appointment (customerId, userId, title, description, location, type, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                "VALUES ("+customerID+", "+ currentUser.getUserID()+", '"+title+"', '"+description+"', '"+location+"', '"+type+"', '', '', '"+startTimeStamp+"', '"+endTimeStamp+"', " +
                "CURRENT_TIMESTAMP, '"+ currentUser.getUserID()+"', CURRENT_TIMESTAMP, '"+ currentUser.getUserID()+"');";
        QueryDB.query(query);
    }
    public static void editAppointment(int appointmentID, int customerID, String title, ZonedDateTime start, ZonedDateTime end, String type, String location, String description){
        Timestamp startTimeStamp = Timestamp.valueOf(start.toLocalDateTime());
        Timestamp endTimeStamp = Timestamp.valueOf(end.toLocalDateTime());
        String query = "UPDATE appointment " +
                "SET customerId = '"+customerID+"', userId = '"+currentUser.getUserID()+"', title = '"+title+"', " +
                "description = '"+description+"', location = '"+location+"', start = '"+startTimeStamp+"' , end = '"+endTimeStamp+"' " +
                "WHERE appointmentId = "+appointmentID;
        QueryDB.query(query);
    }

    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> allAppointments= FXCollections.observableArrayList();
        String query = "SELECT * FROM appointment WHERE userId = "+Main.currentUser.getUserID()+" ORDER BY start;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try{
            while (result.next()){
                Customer customer = CustomerDB.searchCustomer(result.getInt("customerId"));
                Timestamp startTimeStamp = result.getTimestamp("start");
                Timestamp endTimeStamp = result.getTimestamp("end");
                ZonedDateTime startUTC = startTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                ZonedDateTime endUTC = endTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                LocalDateTime startLocal = startUTC.toLocalDateTime();
                LocalDateTime endLocal = endUTC.toLocalDateTime();

                Appointment appointment = new Appointment();
                appointment.setAppointmentId(result.getInt("appointmentId"));
                appointment.setCustomer(customer);
                appointment.setUser(Main.currentUser);
                appointment.setTitle(result.getString("title"));
                appointment.setType(result.getString("type"));
                appointment.setLocation(result.getString("location"));
                appointment.setDescription(result.getString("description"));
                appointment.setStart(startLocal.format(df));
                appointment.setEnd(endLocal.format(df));
                allAppointments.add(appointment);
            }
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return allAppointments;
    }
    public static Appointment searchAppointment(int appointmentID){
        Appointment appointment = new Appointment();
        String query = "SELECT * FROM appointment WHERE appointmentId = "+appointmentID;
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try {
            if (result.next()) {
                Customer customer = CustomerDB.searchCustomer(result.getInt("customerId"));
                Timestamp startTimeStamp = result.getTimestamp("start");
                Timestamp endTimeStamp = result.getTimestamp("end");
                ZonedDateTime startUTC = startTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                ZonedDateTime endUTC = endTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                LocalDateTime startLocal = startUTC.toLocalDateTime();
                LocalDateTime endLocal = endUTC.toLocalDateTime();

                appointment.setAppointmentId(appointmentID);
                appointment.setCustomer(customer);
                appointment.setUser(Main.currentUser);
                appointment.setTitle(result.getString("title"));
                appointment.setType(result.getString("type"));
                appointment.setLocation(result.getString("location"));
                appointment.setDescription(result.getString("description"));
                appointment.setStart(startLocal.format(df));
                appointment.setEnd(endLocal.format(df));
            }
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return appointment;
    }
    public static ObservableList<Appointment> searchAppointment(String search){
        ObservableList<Appointment> foundAppointment = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointment WHERE userId = "+Main.currentUser.getUserID()+" " +
                "AND (type LIKE '%"+search+"%' OR title LIKE '%"+search+"%')  " +
                "ORDER BY start;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try{
            while (result.next()){
                Customer customer = CustomerDB.searchCustomer(result.getInt("customerId"));
                Timestamp startTimeStamp = result.getTimestamp("start");
                Timestamp endTimeStamp = result.getTimestamp("end");
                ZonedDateTime startUTC = startTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                ZonedDateTime endUTC = endTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                LocalDateTime startLocal = startUTC.toLocalDateTime();
                LocalDateTime endLocal = endUTC.toLocalDateTime();

                Appointment appointment = new Appointment();
                appointment.setAppointmentId(result.getInt("appointmentId"));
                appointment.setCustomer(customer);
                appointment.setUser(Main.currentUser);
                appointment.setTitle(result.getString("title"));
                appointment.setType(result.getString("type"));
                appointment.setLocation(result.getString("location"));
                appointment.setDescription(result.getString("description"));
                appointment.setStart(startLocal.format(df));
                appointment.setEnd(endLocal.format(df));
                foundAppointment.add(appointment);
            }
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return foundAppointment;
    }
    public static void deleteAppointment(Appointment appointment){
        String query = "DELETE FROM appointment WHERE appointmentId = "+appointment.getAppointmentId();
        QueryDB.query(query);
    }
    public static void deleteAppointment(int customerID){
        String query = "DELETE FROM appointment WHERE customerId = "+customerID;
        QueryDB.query(query);
    }



}
