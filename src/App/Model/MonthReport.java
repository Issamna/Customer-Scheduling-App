package App.Model;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
public class MonthReport {
    //MonthReport properties
    private String month;
    private String type;
    private String number;

    //empty constructor
    public MonthReport(){}

    //constructor
    public MonthReport(String month, String type, String number){
        this.month = month;
        this.type = type;
        this.number = number;
    }

    //Mutator (setters)
    public void setMonth(String month) {
        this.month = month;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    //Accessors (getters)
    public String getMonth() {
        return month;
    }

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }


}
