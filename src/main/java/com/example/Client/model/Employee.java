package com.example.Client.model;

import java.io.Serializable;

public class Employee implements Serializable {
    private String username;
    private String password;

    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword()
    {
        return password;
    }


    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
