package App.Model;

public class City {
    //City properties
    private int cityID;
    private String cityName;
    private Country country;

    //empty constructor
    public City(){}

    //constructor
    public City(int cityID, String cityName,  Country country){
        this.cityID = cityID;
        this.cityName = cityName;
        this.country = country;
    }

    //Mutator (setters)
    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    //Accessors (getters)
    public int getCityID(){
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public Country getCountry() {
        return country;
    }

}
