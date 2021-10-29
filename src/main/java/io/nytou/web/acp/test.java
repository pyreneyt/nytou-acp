package io.nytou.web.acp;

import org.mindrot.jbcrypt.BCrypt;

public class test {

    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("test", "$2a$10$h.dl5J86rGH7I8bD9bZeZe"));
    }

}
