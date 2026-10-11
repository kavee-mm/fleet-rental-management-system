
package lk.ijse.fleet;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final List<SystemUser> users = new ArrayList<>();

    public boolean registerUser(String username, String password,
                                String fullName) {

        if (username == null || username.isBlank()
                || password == null || password.isBlank()
                || fullName == null || fullName.isBlank()) {
            return false;
        }

        for (SystemUser user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }

        users.add(new SystemUser(username, password, fullName));
        return true;
    }

    public SystemUser login(String username, String password) {

        for (SystemUser user : users) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }
}
