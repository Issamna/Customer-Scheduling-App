package App.Model;

public class Address {
    //Address properties
    private int addressID;
    private String address;
    private String address2;
    private City city;
    private String postalCode;
    private String phone;

    //empty constructor
    public Address(){
    }
    //constructor
    public Address(int addressID, String address, String address2, City city, String postalCode, String phone){
        this.addressID = addressID;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
    }
    //Mutator (setters)
    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //Accessors (getters)
    public int getAddressID() {
        return addressID;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress2() {
        return address2;
    }

    public City getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }



}
