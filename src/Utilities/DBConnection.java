package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static Utilities.Dialog.dialog;

public class DBConnection {


    private static final String url = "jdbc:mysql://3.227.166.251/U07gSn";
    private static final String driver = "com.mysql.jdbc.Driver";
    static Connection conn;

    private static final String username = "U07gSn";
    private static final String password = "53689023397";

    public static Connection start() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection was successful");
        }
        catch (ClassNotFoundException e){
            System.out.println("Error: "+e.getMessage());
            dialog("ERROR","ClassNotFoundException","Error: "+ e.getMessage());
        }
        catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
        return conn;
    }
    public static void close() {
        try {
            conn.close();
            System.out.println("Connection Closed");
        }
        catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
    }


}
