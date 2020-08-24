package com.sm.integramovil.models;

public class CredentialsViewModel {
    public String username;
    public String password;

    public CredentialsViewModel(String email, String password) {
        this.username = email;
        this.password = password;
    }

    public CredentialsViewModel() {
    }

    public String getEmail() {
        return username;
    }

    public void setEmail(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
