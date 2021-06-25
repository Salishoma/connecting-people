package com.connect.connectingpeople.ui.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequestModel {
    @NotNull(message = "First name field cannot be empty")
    @Size(min = 2, message = "length of character should not be less than 2")
    private String firstName;

    @NotNull(message = "Last name field cannot be empty")
    @Size(min = 2, message = "length of character should not be less than 2")
    private String lastName;

    @NotNull(message = "Please provide your email")
    @Email(message = "Must be a valid email")
    private String email;

    @NotNull(message = "Password field cannot be empty")
    @Size(min = 8, max = 50, message = "Password length must be between 8 and 50")
    private String password;
}
