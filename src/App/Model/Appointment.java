package App.Model;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
public class Appointment {
    //Appointment properties
    private int appointmentId;
    private Customer customer;
    private User user;
    private String title;
    private String type;
    private String location;
    private String description;
    private String start;
    private String end;

    //empty constructor
    public Appointment() {
    }

    //constructor
    public Appointment(int appointmentId, Customer customer, User user, String title, String type, String location, String description, String start, String end){
        this.appointmentId = appointmentId;
        this.customer = customer;
        this.user = user;
        this.title = title;
        this.type = type;
        this.location = location;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    //Mutator (setters)
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //Accessors (getters)
    public int getAppointmentId() {
        return appointmentId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getLocation() {
        return location;
    }

}
