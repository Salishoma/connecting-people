package com.connect.connectingpeople.controller;

import com.connect.connectingpeople.model.UserDto;
import com.connect.connectingpeople.service.UsersService;
import com.connect.connectingpeople.ui.model.CreateUserRequestModel;
import com.connect.connectingpeople.ui.model.CreateUserResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.connect.connectingpeople.enums.ApplicationUserRole.USER;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping(path="/",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
                )
    public ResponseEntity<CreateUserResponseModel> createUser(@RequestBody @Valid CreateUserRequestModel userRequestModel){
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userRequestModel, UserDto.class);
        UserDto createUser = usersService.createUser(userDto);
        CreateUserResponseModel userResponseModel = modelMapper.map(createUser, CreateUserResponseModel.class);
        userResponseModel.setRole(USER);
        return new ResponseEntity<>(userResponseModel, HttpStatus.CREATED);
    }

    @GetMapping(path="/", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<CreateUserResponseModel>> getAllUsers(){
        List<CreateUserResponseModel> users = usersService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path="/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateUserResponseModel> getUser(@PathVariable String userId){
        CreateUserResponseModel user = usersService.findUser(userId);
        if(user == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('profile:write') or principal == #userId")
    @PutMapping(path="/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateUserResponseModel> updateUser(@PathVariable String userId, @RequestBody CreateUserRequestModel recent){
        CreateUserResponseModel createUserResponseModel= usersService.updateUser(userId, recent);
        return new ResponseEntity<>(createUserResponseModel, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('profile:delete') or principal == #userId")
    @DeleteMapping(path="/{userId}")
    public String deleteUser(@PathVariable String userId){
        return usersService.deleteUser(userId) ? "User deleted" : "User does not exist in db";
    }
}
