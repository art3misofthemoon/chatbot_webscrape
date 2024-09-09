package com.example.chatbot;

public class UserProfile {
    private String name;
    private String email;
    private String password;

    // Constructor
    public UserProfile(String email) {
        this.email = email;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setter methods (if needed)
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
