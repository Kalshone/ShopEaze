package com.example.shopeaze;

public abstract class Account {

    String username;
    String password;

    public abstract boolean login(String username, String password);
}
