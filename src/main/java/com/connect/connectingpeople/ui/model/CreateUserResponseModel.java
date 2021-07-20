package com.connect.connectingpeople.ui.model;

import com.connect.connectingpeople.enums.ApplicationUserRole;
import com.connect.connectingpeople.model.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateUserResponseModel {
    private String firstName;

    private String lastName;

    private String email;

    private String UserId;

    private ApplicationUserRole role;

    private List<Post> posts;
}
