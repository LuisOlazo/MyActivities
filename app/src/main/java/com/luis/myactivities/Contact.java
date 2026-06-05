package com.luis.myactivities;

public class Contact {
    private final String fullNames;
    private final String birthdate;
    private final String phone;
    private final String email;
    private final String description;

    public Contact(String fullNames, String birthdate, String phone, String email, String description) {
        this.fullNames = fullNames;
        this.birthdate = birthdate;
        this.phone = phone;
        this.email = email;
        this.description = description;
    }

    public String getFullNames() {
        return fullNames;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

}
