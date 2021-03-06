package com.connect.connectingpeople.service;

import com.connect.connectingpeople.model.UserDto;
import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.ui.model.CreateUserRequestModel;
import com.connect.connectingpeople.ui.model.CreateUserResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    List<CreateUserResponseModel> getUsers();

    CreateUserResponseModel findUser(String userId);

    UserDto findUserByUsername(String username);

    UserEntity findByEmail(String email);

    boolean deleteUser(String userId);

    CreateUserResponseModel updateUser(String userId, CreateUserRequestModel recent);
}
