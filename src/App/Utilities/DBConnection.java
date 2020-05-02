package App.Utilities;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static App.Utilities.Dialog.dialog;

/**
 * Creates Database connection for program
 */
public class DBConnection {
    //Database url
    private static final String url = "jdbc:mysql://3.227.166.251/U07gSn";
    //Database Driver
    private static final String driver = "com.mysql.jdbc.Driver";
    public static Connection conn;
    //username
    private static final String username = "U07gSn";
    //password
    private static final String password = "53689023397";

    /**
     * Create database connection
     */
    public static void start() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Opened");
        }
        catch (ClassNotFoundException e){
            System.out.println("Error: "+e.getMessage());
            dialog("ERROR","ClassNotFoundException","Error: "+ e.getMessage());
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
    }

    /**
     * Close database connection
     */
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
