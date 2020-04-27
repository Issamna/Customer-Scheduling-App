import Utilities.DBConnection;

public class Main {

    public static void main(String[] args) {
        System.out.println("hello");

        DBConnection.start();

        DBConnection.close();
    }

}
