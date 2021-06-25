package com.connect.connectingpeople.service.impl;

import com.connect.connectingpeople.model.UserDto;
import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.repository.UsersRepository;
import com.connect.connectingpeople.service.UsersService;
import com.connect.connectingpeople.ui.model.CreateUserResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UsersService {

    UsersRepository usersRepository;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        UserEntity user = mapper.map(userDetails, UserEntity.class);
        usersRepository.save(user);
        return userDetails;
    }

    @Override
    public List<CreateUserResponseModel> getUsers() {
        ModelMapper modelMapper = new ModelMapper();
        return usersRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, CreateUserResponseModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CreateUserResponseModel findUser(String userId) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity user = usersRepository.findById(userId).orElse(null);
        return user == null ? null : modelMapper.map(user, CreateUserResponseModel.class);
    }
}
