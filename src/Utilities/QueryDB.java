package Utilities;
/*
C195 Performance Assessment
Issam Ahmed
000846138
4/27/2020
*/
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Utilities.DBConnection.conn;
import static Utilities.Dialog.dialog;

public class QueryDB {
    private static String query;
    private static Statement statement;
    private static ResultSet result;

    public static void selectQuery(String q){
        query = q;
        try{
            statement = conn.createStatement();
            result = statement.executeQuery(q);
        }
        catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
            dialog("ERROR","SQL Error","Error: "+ e.getMessage());
        }
    }

    public static ResultSet getResult(){
        return result;
    }
}
