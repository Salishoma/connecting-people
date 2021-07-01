package com.connect.connectingpeople.service.impl;

import com.connect.connectingpeople.model.UserDto;
import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.repository.UsersRepository;
import com.connect.connectingpeople.service.UsersService;
import com.connect.connectingpeople.ui.model.CreateUserResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UsersService {

    UsersRepository usersRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));
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

    public UserDto findUserByUsername(String username){
        UserEntity userEntity = usersRepository.findByEmail(username);
        if(userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new ModelMapper()
                .map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);
        if(userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(
                userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }
}
