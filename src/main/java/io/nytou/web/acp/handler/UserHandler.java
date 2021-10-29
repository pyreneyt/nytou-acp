package io.nytou.web.acp.handler;

import io.nytou.web.acp.Main;
import io.nytou.web.acp.user.BackendUser;
import org.mindrot.jbcrypt.BCrypt;

public class UserHandler {

    public boolean authenticate(final String userName, final String password) {

        for (BackendUser backendUser : Main.instance.getMongoDBDatabase().getBackendUsers()) {
            String hashedPassword = BCrypt.hashpw(password, backendUser.getSalt());
            if (userName.equalsIgnoreCase(backendUser.getUserName()) && backendUser.getPassword().equals(hashedPassword)) {
                return true;
            }
        }

        return false;
    }

}
