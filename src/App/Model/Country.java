package App.Model;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
public class Country {
    //Country properties
    private int countryId;
    private String countryName;

    //empty constructor
    public Country(){
    }

    //constructor
    public Country(int countryID, String countryName){
        this.countryId = countryID;
        this.countryName = countryName;
    }

    //Mutator (setters)
    public void setCountryId(int countryId){
        this.countryId = countryId;
    }

    public void setCountryName(String countryName){
        this.countryName = countryName;
    }

    //Accessors (getters)
    public int getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

}
