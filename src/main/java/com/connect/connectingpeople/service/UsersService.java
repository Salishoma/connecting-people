package com.connect.connectingpeople.service;

import com.connect.connectingpeople.model.UserDto;
import com.connect.connectingpeople.ui.model.CreateUserResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    List<CreateUserResponseModel> getUsers();

    CreateUserResponseModel findUser(String userId);

    UserDto findUserByUsername(String username);
}
