package App.Model;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
public class AverageAppointments {
    //AverageAppointments properties
    private String hour;
    private String count;

    //empty constructor
    public AverageAppointments(){}

    //constructor
    public AverageAppointments(String hour, String count){
        this.hour = hour;
        this.count = count;
    }

    //Mutator (setters)
    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setCount(String count) {
        this.count = count;
    }

    //Accessors (getters)
    public String getHour() {
        return hour;
    }

    public String getCount() {
        return count;
    }
}
