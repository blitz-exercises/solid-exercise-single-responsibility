package com.example.userregistration;

import java.time.LocalDateTime;

public class User {
    private final String email;
    private final String hashedPassword;
    private final LocalDateTime registrationDate;
    private boolean activated;
    private String profileLanguage;
    private String profileTimezone;
    private boolean emailNotificationsEnabled;

    public User(String email, String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.registrationDate = LocalDateTime.now();
        this.activated = false;
        this.profileLanguage = "en";
        this.profileTimezone = "UTC";
        this.emailNotificationsEnabled = true;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getProfileLanguage() {
        return profileLanguage;
    }

    public void setProfileLanguage(String profileLanguage) {
        this.profileLanguage = profileLanguage;
    }

    public String getProfileTimezone() {
        return profileTimezone;
    }

    public void setProfileTimezone(String profileTimezone) {
        this.profileTimezone = profileTimezone;
    }

    public boolean isEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    public void setEmailNotificationsEnabled(boolean emailNotificationsEnabled) {
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }
}


