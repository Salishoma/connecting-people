package com.connect.connectingpeople.service.impl;

import com.connect.connectingpeople.model.SecurityUser;
import com.connect.connectingpeople.model.UserDto;
import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.repository.UsersRepository;
import com.connect.connectingpeople.service.UsersService;
import com.connect.connectingpeople.ui.model.CreateUserRequestModel;
import com.connect.connectingpeople.ui.model.CreateUserResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.connect.connectingpeople.enums.ApplicationUserRole.ADMIN;
import static com.connect.connectingpeople.enums.ApplicationUserRole.USER;

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
        ModelMapper mapper = new ModelMapper();
        UserEntity user = mapper.map(userDetails, UserEntity.class);
        user.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setRole(USER);
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
        UserEntity user = findUserFromRepo(userId);
        return user == null ? null : modelMapper.map(user, CreateUserResponseModel.class);
    }

    private UserEntity findUserFromRepo(String userId){
        return usersRepository.findById(userId).orElse(null);
    }

    public UserDto findUserByUsername(String username){
        UserEntity userEntity = findByEmail(username);
        if(userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new ModelMapper()
                .map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity findByEmail(String username){
        return usersRepository.findByEmail(username);
    }

    @Override
    public CreateUserResponseModel updateUser(String userId, CreateUserRequestModel recent) {
        UserEntity user = usersRepository.findById(userId)
                .orElse(null);
        if(user != null){
            user.setFirstName(recent.getFirstName());
            user.setLastName(recent.getLastName());
            usersRepository.save(user);
            return new ModelMapper().map(user, CreateUserResponseModel.class);
        }
        return null;
    }

    @Override
    public boolean deleteUser(String userId) {
        UserEntity entity = usersRepository.findById(userId)
                .orElse(null);
        if(entity == null){
            return false;
        }
        usersRepository.deleteById(userId);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);
        if(userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        Collection<? extends GrantedAuthority> authorities = userEntity.getRole().equals(ADMIN) ?
                ADMIN.getGrantedAuthorities() : USER.getGrantedAuthorities();

        return new SecurityUser(userEntity, authorities);
    }
}
