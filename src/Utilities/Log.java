package Utilities;
/*
C195 Performance Assessment
Issam Ahmed
000846138
4/27/2020
*/
import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

//Logger class to track login
public class Log {
    private static String logFile = "src/Utilities/loginreport.txt";
    private static PrintWriter writer;

    private static void startLog(){
       try{
           File file = new File(logFile);
           if(!file.exists()) {
               System.out.println("File does not exist, creating");
               file.createNewFile();
           }
           writer = new PrintWriter(new FileOutputStream(new File(logFile), true));

       }catch (Exception e){
           System.out.println(e.getMessage());
       }
    }
    private static void closeLog(){
        writer.close();
    }

    public static void writeLog(String input){
        startLog();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy kk:mm:ss z");
        ZonedDateTime now = ZonedDateTime.now();
        String toWrite = "Time Local: "+now.format(df)+" User Logged in: "+input+"\n";
        writer.append(toWrite);
        closeLog();
    }
}
