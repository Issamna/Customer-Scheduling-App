package App.Utilities;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import App.Model.Address;
import App.Model.City;
import App.Model.Country;
import App.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import static App.Main.currentUser;
import static App.Utilities.Dialog.dialog;
/**
 * CustomerDB
 * Database query for customers
 */
public class CustomerDB {

    /**
     * Create customer in database
     * @param customerName
     * @param address
     * @param address2
     * @param city
     * @param country
     * @param postalCode
     * @param phone
     */
    public static void createCustomer(String customerName, String address, String address2, String city, String country, String postalCode, String phone){
        //create address in address table associated with the customer
        Address customerAddress = createAddress(address, address2, city, country, postalCode, phone);
        //Query and run query
        String query = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"+
                "VALUES ('"+customerName+"', "+customerAddress.getAddressId()+", 1, CURRENT_TIMESTAMP, '"+ currentUser.getUserName()+"', CURRENT_TIMESTAMP, '"+currentUser.getUserName()+"')";
        QueryDB.query(query);
    }

    /**
     * Create address in address table and returns it as an Address object
     * @param address
     * @param address2
     * @param city
     * @param country
     * @param postalCode
     * @param phone
     * @return Address saved as a address object
     */
    private static Address createAddress(String address, String address2, String city, String country, String postalCode, String phone){
        //city associated to the Address
        City cityReturn = validateCity(city, country);
        Address addressReturn = new Address();
        //Query and run query
        String query = "INSERT INTO address (address, address2, cityID, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)"+
                "VALUES ('"+address+"', '"+address2+"', "+cityReturn.getCityId()+", '"+postalCode+"', '"+phone+"',  CURRENT_TIMESTAMP, '"+currentUser.getUserName()+"', CURRENT_TIMESTAMP, '"+ currentUser.getUserName()+"')";
        QueryDB.query(query);
        //Query to get the appointment id
        query = "SELECT LAST_INSERT_ID() FROM address";
        QueryDB.returnQuery(query);
        ResultSet result = QueryDB.getResult();
        try {
            result.next();
            //create new address object
            addressReturn = new Address(Integer.parseInt(result.getString(1)), address, address2, cityReturn, postalCode, phone);
        }
        catch(SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return addressReturn;
    }

    /**
     * Method checks if the city exists, otherwise creates a new one (recursion)
     * @param cityInput
     * @param countryInput
     * @return validated City object
     */
    private static City validateCity(String cityInput, String countryInput){
        City cityReturn = new City();
        //country associated to the city
        Country country = validateCountry(countryInput);
        String query;
        try{
            //Query and run query
            query = "SELECT * FROM city WHERE city ='"+cityInput+"' AND countryId = "+country.getCountryId()+"";
            QueryDB.returnQuery(query);
            ResultSet result = QueryDB.getResult();
            //if exists create city object to return
            if(result.next()){
                cityReturn.setCityId(result.getInt("cityID"));
                cityReturn.setCityName(result.getString("city"));
                cityReturn.setCountry(country);
            }
            //if doesn't exist method creates the city
            else {
                query = "INSERT INTO city (city, countryID, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                        "VALUES ('"+cityInput+"', "+country.getCountryId()+", CURRENT_TIMESTAMP, '"+currentUser.getUserName()+"', CURRENT_TIMESTAMP, '"+ currentUser.getUserName()+"')";
                QueryDB.query(query);
                //recursion to validate city exists in database and creates city object
                cityReturn = validateCity(cityInput, country.getCountryName());
            }
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return cityReturn;
    }

    /**
     * Method checks if the country exists, otherwise creates a new one (recursion)
     * @param countryInput
     * @return validated country
     */
    private static Country validateCountry(String countryInput){
        Country countryReturn = new Country();
        String query;
        try {
            //Query and run query
            query = "SELECT * FROM country WHERE country ='"+countryInput+"'";
            QueryDB.returnQuery(query);
            ResultSet result = QueryDB.getResult();
            //if exists create country object to return
            if(result.next()){
                countryReturn.setCountryId(result.getInt("countryID"));
                countryReturn.setCountryName(result.getString("country"));
            }
            //if doesn't exist method creates the country
            else{
                query =  "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                        "VALUES ('"+countryInput+"', CURRENT_TIMESTAMP, '"+currentUser.getUserName()+"', CURRENT_TIMESTAMP, '"+ currentUser.getUserName()+"')";
                QueryDB.query(query);
                //recursion to validate country exists in database and creates city object
                countryReturn = validateCountry(countryInput);
            }
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return countryReturn;
    }

    /**
     * Edit customer in database
     * @param customerID
     * @param addressID
     * @param customerName
     * @param address
     * @param address2
     * @param city
     * @param country
     * @param postalCode
     * @param phone
     */
    public static void editCustomer(int customerID, int addressID, String customerName, String address, String address2, String city, String country, String postalCode, String phone){
        //edit address in address table associated with the customer
        editAddress(addressID, address, address2, city, country, postalCode, phone);
        //Query and run query
        String query = "UPDATE customer " +
                "SET customerName = '"+customerName+"' "+
                "WHERE customerId = "+customerID+" AND addressID = "+addressID;
        QueryDB.query(query);
    }

    /**
     * Edit address associated with the customer
     * @param addressID
     * @param address
     * @param address2
     * @param city
     * @param country
     * @param phone
     * @param postalCode
     */
    private static void editAddress(int addressID, String address, String address2, String city, String country, String phone, String postalCode){
        //city associated to the Address
        City cityReturn = validateCity(city, country);
        //Query and run query
        String query = "UPDATE address " +
                    "SET address = '"+address+"', address2 = '"+address2+"', cityId = "+cityReturn.getCityId()+", postalCode = '"+postalCode+"', phone = '"+phone+"' " +
                    "WHERE addressID = "+addressID;
        QueryDB.query(query);
    }

    /**
     * Delete customer in database
     * @param customer
     */
    public static void deleteCustomer(Customer customer){
        //delete any appointments associated with customer
        AppointmentDB.deleteAppointment(customer.getCustomerId());
        //delete address associated with customer
        int addressId = customer.getAddress().getAddressId();
        //Query delete address and run query
        String query = "DELETE FROM customer WHERE customerId = "+customer.getCustomerId();
        QueryDB.query(query);
        //Query delete customer and run query
        query = "DELETE FROM address WHERE addressId = "+addressId;
        QueryDB.query(query);
    }

    /**
     * Method to return all customers
     * @return ObservableList<Customer> list of all customers
     */
    public static ObservableList<Customer> getAllCustomers(){
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        //Query and run query
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
            //get all results
            while (result.next()) {
                int customerID = result.getInt("customerId");
                String customerName = result.getString("customerName");
                Country country = new Country(result.getInt("countryId"), result.getString("country"));
                City city = new City(result.getInt("cityId"), result.getString("city"), country);
                Address address = new Address();
                address.setAddressId(result.getInt("addressId"));
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

    /**
     * search customer with customer id
     * @param customerID
     * @return searched customer object
     */
    public static Customer searchCustomer(int customerID){
        Customer customer = null;
        //Query and run query
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
            //get result
            if (result.next()) {
                String customerName = result.getString("customerName");
                Country country = new Country(result.getInt("countryId"), result.getString("country"));
                City city = new City(result.getInt("cityId"), result.getString("city"), country);
                Address address = new Address();
                address.setAddressId(result.getInt("addressId"));
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

    /**
     * Search customer with name
     * @param searchName
     * @return ObservableList<Customer> list of all customers that matches string
     */
    public static ObservableList<Customer> searchCustomers(String searchName){
        ObservableList<Customer> foundCustomers = FXCollections.observableArrayList();
        //Query and run query
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
            //get all the results
            while (result.next()) {
                int customerID = result.getInt("customerId");
                String customerName = result.getString("customerName");
                Country country = new Country(result.getInt("countryId"), result.getString("country"));
                City city = new City(result.getInt("cityId"), result.getString("city"), country);
                Address address = new Address();
                address.setAddressId(result.getInt("addressId"));
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
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return foundCustomers;
    }




}
