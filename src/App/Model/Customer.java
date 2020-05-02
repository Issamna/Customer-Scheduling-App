package App.Model;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
public class Customer {
    //Customer properties
    private int customerId;
    private String customerName;
    private Address address;

    //empty constructor
    public Customer(){
    }

    //constructor
    public Customer(int customerId, String customerName, Address address){
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
    }

    //Mutator (setters)
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    //Accessors (getters)
    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Address getAddress() {
        return address;
    }
}
