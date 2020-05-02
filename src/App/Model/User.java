package App.Model;
/*
C195 Performance Assessment
Issam Ahmed
000846138
5/02/2020
*/
public class User {
    //User properties
    private int userId;
    private String userName;
    private String password;

    //empty constructor
    public User(){}
    //constructor
    public User(int userID, String userName, String password){
        this.userId = userID;
        this.userName = userName;
        this.password = password;
    }

    //Mutator (setters)
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Accessors (getters)
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
