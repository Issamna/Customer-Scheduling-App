package App.Model;

public class AverageAppointments {
    private String hour;
    private String count;


    public AverageAppointments(){}

    public AverageAppointments(String hour, String count){
        this.hour = hour;
        this.count = count;
    }
    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getHour() {
        return hour;
    }

    public String getCount() {
        return count;
    }
}
