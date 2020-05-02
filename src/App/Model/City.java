package App.Model;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
public class City {
    //City properties
    private int cityId;
    private String cityName;
    private Country country;

    //empty constructor
    public City(){}

    //constructor
    public City(int cityID, String cityName,  Country country){
        this.cityId = cityID;
        this.cityName = cityName;
        this.country = country;
    }

    //Mutator (setters)
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    //Accessors (getters)
    public int getCityId(){
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public Country getCountry() {
        return country;
    }

}
