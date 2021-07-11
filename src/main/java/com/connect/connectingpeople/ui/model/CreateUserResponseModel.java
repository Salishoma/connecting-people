package com.connect.connectingpeople.ui.model;

import com.connect.connectingpeople.enums.ApplicationUserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserResponseModel {
    private String firstName;

    private String lastName;

    private String email;

    private String UserId;

    private ApplicationUserRole role;
}
