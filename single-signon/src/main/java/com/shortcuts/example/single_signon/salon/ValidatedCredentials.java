package com.shortcuts.example.single_signon.salon;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Validated customer credentials required by Shortcuts.
 */
public class ValidatedCredentials {
    private String firstName;
    private String lastName;
    private String email;

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
