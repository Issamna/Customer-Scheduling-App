package App.Utilities;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *Create/Edit user login file
 */
public class Log {
    //file relative path
    private static String logFile = "src/App/Utilities/loginreport.txt";
    private static PrintWriter writer;

    /**
     * Opens/Creates loginreport file
     */
    private static void startLog(){
       try{
           File file = new File(logFile);
           //checks if file exists, if not create one
           if(!file.exists()) {
               System.out.println("File does not exist, creating");
               file.createNewFile();
           }
           writer = new PrintWriter(new FileOutputStream(new File(logFile), true));
       }catch (Exception e){
           System.out.println(e.getMessage());
       }
    }

    /**
     * closes writer
     */
    private static void closeLog(){
        writer.close();
    }

    /**
     * Writes to loginreport
     * @param input
     */
    public static void writeLog(String input){
        startLog();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy kk:mm:ss z");
        //Writes local login time
        ZonedDateTime now = ZonedDateTime.now();
        String toWrite = "Time Local: "+now.format(df)+" User Logged in: "+input+"\n";
        writer.append(toWrite);
        closeLog();
    }
}
