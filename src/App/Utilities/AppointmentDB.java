package App.Utilities;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import App.Main;
import App.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import static App.Main.currentUser;
import static App.Utilities.Dialog.dialog;


/**
 * AppointmentDB
 * Database query for appointments
 */
public class AppointmentDB {
    //Default zoneID
    private static ZoneId zone = ZoneId.systemDefault();
    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");

    /**
     * Create an appointment in database
     * @param customerID
     * @param title
     * @param start
     * @param end
     * @param type
     * @param location
     * @param description
     */
    public static void createAppointment(int customerID, String title, ZonedDateTime start, ZonedDateTime end, String type, String location, String description){
        //convert ZoneDateTime to TimeStamp
        Timestamp startTimeStamp = Timestamp.valueOf(start.toLocalDateTime());
        Timestamp endTimeStamp = Timestamp.valueOf(end.toLocalDateTime());
        //Query
        String query = "INSERT INTO appointment (customerId, userId, title, description, location, type, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                "VALUES ("+customerID+", "+ currentUser.getUserId()+", '"+title+"', '"+description+"', '"+location+"', '"+type+"', '', '', '"+startTimeStamp+"', '"+endTimeStamp+"', " +
                "CURRENT_TIMESTAMP, '"+ currentUser.getUserName()+"', CURRENT_TIMESTAMP, '"+ currentUser.getUserName()+"');";
        //Run query
        QueryDB.query(query);
    }

    /**
     * Edit an appointment in database. Needs appointmentID to function.
     * @param appointmentID
     * @param customerID
     * @param title
     * @param start
     * @param end
     * @param type
     * @param location
     * @param description
     */
    public static void editAppointment(int appointmentID, int customerID, String title, ZonedDateTime start, ZonedDateTime end, String type, String location, String description){
        //convert ZoneDateTime to TimeStamp
        Timestamp startTimeStamp = Timestamp.valueOf(start.toLocalDateTime());
        Timestamp endTimeStamp = Timestamp.valueOf(end.toLocalDateTime());
        //Query
        String query = "UPDATE appointment " +
                "SET customerId = '"+customerID+"', userId = '"+currentUser.getUserId()+"', title = '"+title+"', " +
                "description = '"+description+"', location = '"+location+"', start = '"+startTimeStamp+"' , end = '"+endTimeStamp+"' " +
                "WHERE appointmentId = "+appointmentID;
        //Run query
        QueryDB.query(query);
    }

    /**
     * Checks for appointment overlap.
     * @param start
     * @param end
     * @param appointmentId
     * @return boolean; True if there is an overlap
     */
    public static boolean validateOverlap(ZonedDateTime start, ZonedDateTime end, int appointmentId){
        boolean error = false;
        //convert ZoneDateTime to TimeStamp
        Timestamp startTimeStamp = Timestamp.valueOf(start.toLocalDateTime());
        Timestamp endTimeStamp = Timestamp.valueOf(end.toLocalDateTime());
        //Query to check time and if in edit mode the appointmentID will help save over its own appointment time and not return true
        String query = "SELECT * FROM appointment "+
                "WHERE (('"+startTimeStamp+"' >= start AND '"+startTimeStamp+"' < end) OR ('"+endTimeStamp+"' > start AND '"+endTimeStamp+"' = end)) " +
                "AND (appointmentID != "+appointmentId+")";
        //Run query
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        //Just need one result
        try {
            if(result.next()){
                error = true;
            }
        } catch (SQLException e) {
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return error;
    }

    /**
     * Delete appointment by Object Appointment
     * @param appointment
     */
    public static void deleteAppointment(Appointment appointment){
        //Query and run query
        String query = "DELETE FROM appointment WHERE appointmentId = "+appointment.getAppointmentId();
        QueryDB.query(query);
    }

    /**
     * Delete appointment by Customer ID
     * @param customerID
     */
    public static void deleteAppointment(int customerID){
        //Query and run query
        String query = "DELETE FROM appointment WHERE customerId = "+customerID;
        QueryDB.query(query);
    }

    //Search or get all appointments
    /**
     * Method to return all appointments of the current user in database
     * @return ObservableList<Appointment> list of all appointments of current user
     */
    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> allAppointments= FXCollections.observableArrayList();
        //Query and run query
        String query = "SELECT * FROM appointment WHERE userId = "+Main.currentUser.getUserId()+" ORDER BY start;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try{
            //get all results
            while(result.next()){
                //search customer table to get customer associated with appointment
                Customer customer = CustomerDB.searchCustomer(result.getInt("customerId"));
                //convert start and end timestamp in UTC to local time
                Timestamp startTimeStamp = result.getTimestamp("start");
                Timestamp endTimeStamp = result.getTimestamp("end");
                ZonedDateTime startUTC = startTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                ZonedDateTime endUTC = endTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                LocalDateTime startLocal = startUTC.toLocalDateTime();
                LocalDateTime endLocal = endUTC.toLocalDateTime();
                //create new appointment and add the object properties
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
                //add to list
                allAppointments.add(appointment);
            }
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return allAppointments;
    }

    /**
     * Return appointment associated with that ID
     * @param appointmentID
     * @return appointment
     */
    public static Appointment searchAppointment(int appointmentID){
        Appointment appointment = new Appointment();
        //Query and run query
        String query = "SELECT * FROM appointment WHERE appointmentId = "+appointmentID;
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try {
            //only one result
            if (result.next()) {
                //search customer table to get customer associated with appointment
                Customer customer = CustomerDB.searchCustomer(result.getInt("customerId"));
                //convert start and end timestamp in UTC to local time
                Timestamp startTimeStamp = result.getTimestamp("start");
                Timestamp endTimeStamp = result.getTimestamp("end");
                ZonedDateTime startUTC = startTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                ZonedDateTime endUTC = endTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                LocalDateTime startLocal = startUTC.toLocalDateTime();
                LocalDateTime endLocal = endUTC.toLocalDateTime();
                //Add the object properties
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

    //Report database queries(all appointment related)

    /**
     * Queries required month & type information
     * @return Month Report list
     * @throws SQLException
     */
    public static ObservableList<MonthReport> getMonthsReport()throws SQLException {
        ObservableList<MonthReport> monthReports = FXCollections.observableArrayList();
        //Query and run query
        String query = "SELECT MONTHNAME(`start`), type, COUNT(*) FROM appointment GROUP BY MONTHNAME(`start`), type;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        while (result.next()){
            //create new MonthReport object and add properties
            String month = result.getString("MONTHNAME(`start`)");
            String type = result.getString("type");
            String number = result.getString("COUNT(*)");
            monthReports.add(new MonthReport(month, type, number));
        }
        return monthReports;
    }

    /**
     * Method to return all appointments of all users in database
     * @return ObservableList<Appointment> list of all appointments of all users
     */
    public static ObservableList<Appointment> getAllAppointmentsAllUsers(){
        ObservableList<Appointment> allAppointments= FXCollections.observableArrayList();
        //Query and run query
        String query = "SELECT * FROM appointment ORDER BY start;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try{
            while (result.next()){
                //search customer table to get customer associated with appointment
                Customer customer = CustomerDB.searchCustomer(result.getInt("customerId"));
                //convert start and end timestamp in UTC to local time
                Timestamp startTimeStamp = result.getTimestamp("start");
                Timestamp endTimeStamp = result.getTimestamp("end");
                ZonedDateTime startUTC = startTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                ZonedDateTime endUTC = endTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                LocalDateTime startLocal = startUTC.toLocalDateTime();
                LocalDateTime endLocal = endUTC.toLocalDateTime();
                //create new appointment and add the object properties
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

    /**
     * Method to return all appointments of a user in database
     * @param userName
     * @return ObservableList<Appointment> list of all appointments of all users
     */
    public static ObservableList<Appointment> getAppointmentsByUser(String userName){
        ObservableList<Appointment> allAppointments= FXCollections.observableArrayList();
        //Query and run query
        String query = "SELECT * FROM appointment, user WHERE appointment.userId = user.userId AND user.userName = '"+userName+"' ORDER BY start;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try{
            while (result.next()){
                //search customer table to get customer associated with appointment
                Customer customer = CustomerDB.searchCustomer(result.getInt("customerId"));
                //convert start and end timestamp in UTC to local time
                Timestamp startTimeStamp = result.getTimestamp("start");
                Timestamp endTimeStamp = result.getTimestamp("end");
                ZonedDateTime startUTC = startTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                ZonedDateTime endUTC = endTimeStamp.toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(zone);
                LocalDateTime startLocal = startUTC.toLocalDateTime();
                LocalDateTime endLocal = endUTC.toLocalDateTime();
                //create new appointment and add the object properties
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

    /**
     * Queries required average appointments information
     * @return AverageAppointments list
     * @throws SQLException
     */
    public static ObservableList<AverageAppointments> getAverageAppointments() throws SQLException{
        ObservableList<AverageAppointments> averageAppointments = FXCollections.observableArrayList();
        //Query and run query for total count
        String query ="SELECT count(*) AS Total FROM appointment;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        result.next();
        double total = result.getInt("Total");
        //Query and run query for the hour and how many appointments at that hour
        query ="SELECT HOUR(start) AS Hour, COUNT(start) AS Count FROM appointment GROUP BY Hour ORDER BY hour;";
        QueryDB.returnQuery(query);
        result = QueryDB.getResult();
        //date and decimal formatter for hour
        DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("hh:mm a");
        DecimalFormat formatter = new DecimalFormat("#0.00");
        while(result.next()){
            int hourSQL = result.getInt("Hour");
            //hour comes in UTC so need to convert it to local
            ZonedDateTime hourStart = ZonedDateTime.of(LocalDate.now(ZoneId.of("UTC")), LocalTime.of(hourSQL, 0),  ZoneId.of("UTC")).withZoneSameInstant(zone);
            //hour end adds 59mins to the hour start
            ZonedDateTime hourEnd = hourStart.plusMinutes(59);
            //Comes out as 8:00 AM - 8:59AM format
            String duration = hourStart.format(hourFormat) +" - "+hourEnd.format(hourFormat);
            //gets the count for that hour and converts it to a percentage of the total appointments
            double averageRow = (result.getInt("Count")/total)*100;
            AverageAppointments hourRow = new AverageAppointments(duration, formatter.format(averageRow)+"%");
            averageAppointments.add(hourRow);
        }
        return averageAppointments;
    }
}
