package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private boolean admin;
    private String firstname;
    private String lastname;
    private String email;

    public User() {
    }

    public User(final String username,
                final String password) {
        this.username = username;
        this.password = password;
    }

    public User(final String username,
                final String password,
                final boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public User(final String username,
                final String password,
                final boolean admin,
                final String firstname,
                final String lastname,
                final String email) {
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
