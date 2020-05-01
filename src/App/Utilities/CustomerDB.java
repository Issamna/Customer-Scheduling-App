package App.Utilities;

import App.Model.Address;
import App.Model.City;
import App.Model.Country;
import App.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

import static App.Utilities.Dialog.dialog;

public class CustomerDB {

    public static void createCustomer(String customerName, String address, String address2, String city, String country, String postalCode, String phone){
        Address customerAddress = createAddress(address, address2, city, country, postalCode, phone);
        String query = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"+
                "VALUES ('"+customerName+"', "+customerAddress.getAddressID()+", 1, CURRENT_TIMESTAMP, 'Admin', DEFAULT, 'Admin')";
        QueryDB.query(query);
    }
    private static Address createAddress(String address, String address2, String city, String country, String postalCode, String phone){
        City cityReturn = validateCity(city, country);
        Address addressReturn = new Address();
        String query = "INSERT INTO address (address, address2, cityID, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)"+
                "VALUES ('"+address+"', '"+address2+"', "+cityReturn.getCityID()+", '"+postalCode+"', '"+phone+"',  CURRENT_TIMESTAMP, 'Admin', DEFAULT, 'Admin')";
        QueryDB.query(query);

        query = "SELECT LAST_INSERT_ID() FROM address";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try {
            result.next();
            addressReturn = new Address(Integer.parseInt(result.getString(1)), address, address2, cityReturn, postalCode, phone);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }

        return addressReturn;
    }
    private static Country validateCountry(String countryInput){
        Country countryReturn = new Country();
        String query;
        try {
            query = "SELECT * FROM country WHERE country ='"+countryInput+"'";
            QueryDB.returnQuery(query);
            ResultSet result = QueryDB.getResult();
            if(result.next()){
                countryReturn.setCountryID(result.getInt("countryID"));
                countryReturn.setCountryName(result.getString("country"));
            }
            else{
                query =  "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                        "VALUES ('"+countryInput+"', CURRENT_TIMESTAMP, 'Admin', DEFAULT, 'Admin')";
                QueryDB.query(query);
                countryReturn = validateCountry(countryInput);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return countryReturn;
    }
    private static City validateCity(String cityInput, String countryInput){
        City cityReturn = new City();
        Country country = validateCountry(countryInput);
        String query;
        try{
            query = "SELECT * FROM city WHERE city ='"+cityInput+"' AND countryId = "+country.getCountryID()+"";
            QueryDB.returnQuery(query);
            ResultSet result = QueryDB.getResult();
            if(result.next()){
                cityReturn.setCityID(result.getInt("cityID"));
                cityReturn.setCityName(result.getString("city"));
                cityReturn.setCountry(country);
            }
            else {
                query = "INSERT INTO city (city, countryID, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                        "VALUES ('"+cityInput+"', "+country.getCountryID()+", CURRENT_TIMESTAMP, 'Admin', DEFAULT, 'Admin')";
                QueryDB.query(query);
                cityReturn = validateCity(cityInput, country.getCountryName());
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return cityReturn;
    }
    public static void editCustomer(int customerID, int addressID, String customerName, String address, String address2, String city, String country, String postalCode, String phone){
        editAddress(addressID, address, address2, city, country, postalCode, phone);
        String query = "UPDATE customer " +
                "SET customerName = '"+customerName+"' "+
                "WHERE customerId = "+customerID+" AND addressID = "+addressID;
        QueryDB.query(query);
    }
    private static void editAddress(int addressID, String address, String address2, String city, String country, String phone, String postalCode){
        City cityReturn = validateCity(city, country);
        String query = "UPDATE address " +
                    "SET address = '"+address+"', address2 = '"+address2+"', cityId = "+cityReturn.getCityID()+", postalCode = '"+postalCode+"', phone = '"+phone+"' " +
                    "WHERE addressID = "+addressID;
        QueryDB.query(query);
    }

    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String query = "SELECT customer.customerId, customer.customerName, " +
                "address.addressId, address.address, address.address2, address.postalCode, address.phone, " +
                "city.cityId, city.city, " +
                "country.countryId, country.country " +
                "FROM customer, address, city, country " +
                "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId " +
                "ORDER BY customer.customerName;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try {
            while (result.next()) {
                int customerID = result.getInt("customerId");
                String customerName = result.getString("customerName");
                Country country = new Country(result.getInt("countryId"), result.getString("country"));
                City city = new City(result.getInt("cityId"), result.getString("city"), country);
                Address address = new Address();
                address.setAddressID(result.getInt("addressId"));
                address.setAddress(result.getString("address"));
                address.setAddress2(result.getString("address2"));
                address.setCity(city);
                address.setPostalCode(result.getString("postalCode"));
                address.setPhone(result.getString("phone"));
                Customer customer = new Customer(customerID, customerName, address);
                allCustomers.add(customer);
            }
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return allCustomers;
    }
    public static Customer searchCustomer(int customerID){
        Customer customer = null;
        String query = "SELECT customer.customerId, customer.customerName, " +
                "address.addressId, address.address, address.address2, address.postalCode, address.phone, " +
                "city.cityId, city.city, " +
                "country.countryId, country.country " +
                "FROM customer, address, city, country " +
                "WHERE customer.customerId = "+customerID+" AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId " +
                "ORDER BY customer.customerName;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try {
            if (result.next()) {
                String customerName = result.getString("customerName");
                Country country = new Country(result.getInt("countryId"), result.getString("country"));
                City city = new City(result.getInt("cityId"), result.getString("city"), country);
                Address address = new Address();
                address.setAddressID(result.getInt("addressId"));
                address.setAddress(result.getString("address"));
                address.setAddress2(result.getString("address2"));
                address.setCity(city);
                address.setPostalCode(result.getString("postalCode"));
                address.setPhone(result.getString("phone"));
                customer = new Customer(customerID, customerName, address);
            }
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return customer;
    }
    public static ObservableList<Customer> searchCustomers(String searchName){
        ObservableList<Customer> foundCustomers = FXCollections.observableArrayList();
        String query = "SELECT customer.customerId, customer.customerName, " +
                "address.addressId, address.address, address.address2, address.postalCode, address.phone, " +
                "city.cityId, city.city, " +
                "country.countryId, country.country " +
                "FROM customer, address, city, country " +
                "WHERE customer.customerName LIKE '%"+searchName+"%' AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId " +
                "ORDER BY customer.customerName;";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try {
            while (result.next()) {
                int customerID = result.getInt("customerId");
                String customerName = result.getString("customerName");
                Country country = new Country(result.getInt("countryId"), result.getString("country"));
                City city = new City(result.getInt("cityId"), result.getString("city"), country);
                Address address = new Address();
                address.setAddressID(result.getInt("addressId"));
                address.setAddress(result.getString("address"));
                address.setAddress2(result.getString("address2"));
                address.setCity(city);
                address.setPostalCode(result.getString("postalCode"));
                address.setPhone(result.getString("phone"));
                Customer customer = new Customer(customerID, customerName, address);
                foundCustomers.add(customer);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return foundCustomers;
    }
    public static void deleteCustomer(Customer customer){
        AppointmentDB.deleteAppointment(customer.getCustomerId());
        int addressId = customer.getAddress().getAddressID();
       String query = "DELETE FROM customer WHERE customerId = "+customer.getCustomerId();
       QueryDB.query(query);
       query = "DELETE FROM address WHERE addressId = "+addressId;
       QueryDB.query(query);
    }



}
