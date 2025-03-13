package api;

public class User {
    private String userID;
    private String password;
    private Role role;

    public User(String userID, String password, Role role) {
        this.userID = userID;
        this.password = password;
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public Role getRole() {
        return role;
    }

    public boolean isValidPassword(String inputPassword) {
        return password.equals(inputPassword);
    }
}
