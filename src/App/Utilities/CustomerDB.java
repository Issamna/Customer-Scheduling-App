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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static App.Main.currentUser;
import static App.Utilities.Dialog.dialog;
/**
 * CustomerDB
 * Database query for customers
 * uses prepared statements for creating (INSERT) and editing (UPDATE) to deal with user inputs
 * uses statements for the rest
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
     * @return boolean if the statement was processed
     */
    public static boolean createCustomer(String customerName, String address, String address2, String city, String country, String postalCode, String phone){
        boolean error = true;
        //create address in address table associated with the customer
        Address customerAddress = createAddress(address, address2, city, country, postalCode, phone);
        try{
            String query = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"+
                   "VALUES (?, ?, 1, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
            PreparedStatement statement = DBConnection.conn.prepareStatement(query);
            statement.setString(1, customerName);
            statement.setInt(2, customerAddress.getAddressId());
            statement.setString(3, currentUser.getUserName());
            statement.setString(4, currentUser.getUserName());
            statement.executeUpdate();
        }catch (SQLException e){
            error = false;
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return error;
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
        try {
            //Query and run query
            String query = "INSERT INTO address (address, address2, cityID, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)"+
                    "VALUES (?, ?, ?, ?, ?,  CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
            PreparedStatement statement = DBConnection.conn.prepareStatement(query);
            statement.setString(1, address);
            statement.setString(2, address2);
            statement.setInt(3, cityReturn.getCityId());
            statement.setString(4, postalCode);
            statement.setString(5, phone);
            statement.setString(6, currentUser.getUserName());
            statement.setString(7, currentUser.getUserName());
            statement.executeUpdate();
            //Query to get the appointment id
            query = "SELECT LAST_INSERT_ID() FROM address";
            statement = DBConnection.conn.prepareStatement(query);
            ResultSet result = statement.executeQuery();
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
            query = "SELECT * FROM city WHERE city = ? AND countryId = ?";
            PreparedStatement statement = DBConnection.conn.prepareStatement(query);
            statement.setString(1, cityInput);
            statement.setInt(2, country.getCountryId());
            ResultSet result = statement.executeQuery();
            //if exists create city object to return
            if(result.next()){
                cityReturn.setCityId(result.getInt("cityID"));
                cityReturn.setCityName(result.getString("city"));
                cityReturn.setCountry(country);
            }
            //if doesn't exist method creates the city
            else {
                query =  "INSERT INTO city (city, countryID, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                        "VALUES (?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
                statement = DBConnection.conn.prepareStatement(query);
                statement.setString(1, cityInput);
                statement.setInt(2, country.getCountryId());
                statement.setString(3, currentUser.getUserName());
                statement.setString(4, currentUser.getUserName());
                statement.executeUpdate();
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
            query = "SELECT * FROM country WHERE country = ?";
            PreparedStatement statement = DBConnection.conn.prepareStatement(query);
            statement.setString(1, countryInput);
            ResultSet result = statement.executeQuery();
            //if exists create country object to return
            if(result.next()){
                countryReturn.setCountryId(result.getInt("countryID"));
                countryReturn.setCountryName(result.getString("country"));
            }
            //if doesn't exist method creates the country
            else{
                query =  "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                        "VALUES (?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
                statement = DBConnection.conn.prepareStatement(query);
                statement.setString(1, countryInput);
                statement.setString(2, currentUser.getUserName());
                statement.setString(3, currentUser.getUserName());
                statement.executeUpdate();
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
     * @return boolean if the statement was processed
     */
    public static boolean editCustomer(int customerID, int addressID, String customerName, String address, String address2, String city, String country, String postalCode, String phone){
        boolean error = true;
        //edit address in address table associated with the customer
        editAddress(addressID, address, address2, city, country, postalCode, phone);
        //Query and run query
        try{
            String query = "UPDATE customer SET customerName = ? WHERE customerId = ? AND addressID = ?";
            PreparedStatement statement = DBConnection.conn.prepareStatement(query);
            statement.setString(1, customerName);
            statement.setInt(2, customerID);
            statement.setInt(3, addressID);
            statement.executeUpdate();
        }catch (SQLException e){
            error = false;
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return error;
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
        try {
            String query = "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ? WHERE addressID = ?";
            PreparedStatement statement = DBConnection.conn.prepareStatement(query);
            statement.setString(1, address);
            statement.setString(2, address2);
            statement.setInt(3, cityReturn.getCityId());
            statement.setString(4, postalCode);
            statement.setString(5, phone);
            statement.setInt(6,addressID);
            statement.executeUpdate();
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
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
        try {
            //Query and run query
            String query = "SELECT customer.customerId, customer.customerName, " +
                    "address.addressId, address.address, address.address2, address.postalCode, address.phone, " +
                    "city.cityId, city.city, " +
                    "country.countryId, country.country " +
                    "FROM customer, address, city, country " +
                    "WHERE customer.customerName LIKE ? AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId " +
                    "ORDER BY customer.customerName;";
            PreparedStatement statement = DBConnection.conn.prepareStatement(query);
            statement.setString(1, "%"+searchName+"%");
            ResultSet result = statement.executeQuery();
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
