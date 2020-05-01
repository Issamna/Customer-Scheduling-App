package App.Model;

public class MonthReport {
    private String month;
    private String type;
    private String number;

    public MonthReport(){}

    public MonthReport(String month, String type, String number){
        this.month = month;
        this.type = type;
        this.number = number;
    }
    public void setMonth(String month) {
        this.month = month;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNumber(String number) {
        this.number = number;
    }

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
