package io.nytou.web.acp.user;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class BackendUser extends UserData {

    @BsonProperty(value = "userName")
    private String userName;

    @BsonProperty(value = "uuid")
    private String uuid;

    @BsonProperty(value = "role")
    private String role;

    @BsonProperty(value = "password")
    private String password;

    @BsonProperty(value = "salt")
    public String salt;

    public BackendUser(String userName, String uuid, String role, String password, String salt) {
        this.userName = userName;
        this.uuid = uuid;
        this.role = role;
        this.password = password;
        this.salt = salt;
    }

    public BackendUser() {}

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
