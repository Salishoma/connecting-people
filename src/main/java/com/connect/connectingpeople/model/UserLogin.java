package com.connect.connectingpeople.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLogin  implements Serializable {
    private String email;
    private String password;
}
