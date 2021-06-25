package com.connect.connectingpeople.service;

import com.connect.connectingpeople.model.UserDto;
import com.connect.connectingpeople.ui.model.CreateUserResponseModel;

import java.util.List;

public interface UsersService {
    UserDto createUser(UserDto userDto);

    List<CreateUserResponseModel> getUsers();

    CreateUserResponseModel findUser(String userId);
}
