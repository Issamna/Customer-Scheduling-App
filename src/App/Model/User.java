package App.Model;

/*
C195 Performance Assessment
Issam Ahmed
000846138
4/27/2020
*/

public class User {
    //User properties
    private int userID;
    private String userName;
    private String password;

    //empty constructor
    public User(){

    }
    //constructor
    public User(int userID, String userName, String password){
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }

    //Mutator (setters)
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Accessors (getters)

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
