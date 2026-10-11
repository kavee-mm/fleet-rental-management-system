
package lk.ijse.fleet;

public class SystemUser {

    private String username;
    private String password;
    private String fullName;

    public SystemUser(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}
