package App.Model;

public class Country {
    //User properties
    private int countryID;
    private String countryName;

    //empty constructor
    public Country(){
    }

    //constructor
    public Country(int countryID, String countryName){
        this.countryID = countryID;
        this.countryName = countryName;
    }

    //Mutator (setters)
    public void setCountryID(int countryID){
        this.countryID = countryID;
    }

    public void setCountryName(String countryName){
        this.countryName = countryName;
    }

    //Accessors (getters)
    public int getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }

}
