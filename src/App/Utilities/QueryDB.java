package App.Utilities;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static App.Utilities.DBConnection.conn;
import static App.Utilities.Dialog.dialog;

/**
 * Query Database class
 */
public class QueryDB {
    private static String query;
    private static Statement statement;
    private static ResultSet result;

    /**
     * Create query which returns results (SELECT Queries)
     * @param q
     */
    public static void returnQuery(String q){
        query = q;
        try{
            statement = conn.createStatement();
            result = statement.executeQuery(q);
        }
        catch (SQLException e){
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
    }

    /**
     * Create query for database (INSERT, UPDATE, etc Queries)
     * @param q
     */
    public static void query(String q){
        query = q;
        try{
            statement = conn.createStatement();
            statement.executeUpdate(q);
        }
        catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
    }

    /**
     * Method to return query results
     * @return results from query
     */
    public static ResultSet getResult(){
        return result;
    }
}
