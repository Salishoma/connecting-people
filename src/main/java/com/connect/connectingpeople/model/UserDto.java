package com.connect.connectingpeople.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 7507638027229979629L;

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private String userId;
    private String encryptedPassword;

}
