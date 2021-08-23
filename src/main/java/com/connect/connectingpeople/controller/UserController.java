package com.connect.connectingpeople.controller;

import com.connect.connectingpeople.model.UserDto;
import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.model.UserLogin;
import com.connect.connectingpeople.service.UsersService;
import com.connect.connectingpeople.ui.model.CreateUserRequestModel;
import com.connect.connectingpeople.ui.model.CreateUserResponseModel;
import com.connect.connectingpeople.utils.JwtResponse;
import com.connect.connectingpeople.utils.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.connect.connectingpeople.enums.ApplicationUserRole.USER;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UsersService usersService,
                          AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(path="",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
                )
    public ResponseEntity<CreateUserResponseModel> createUser(@RequestBody @Valid CreateUserRequestModel userRequestModel){
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userRequestModel, UserDto.class);
        UserDto createUser = usersService.createUser(userDto);
        if(createUser == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        CreateUserResponseModel userResponseModel = modelMapper.map(createUser, CreateUserResponseModel.class);
        userResponseModel.setRole(USER);
        return new ResponseEntity<>(userResponseModel, HttpStatus.CREATED);
    }

    @GetMapping(path="", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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

    @PreAuthorize("hasAuthority('profile:write') or principal == @emailSearch.getEmailFromId(#userId)")
    @PutMapping(path="/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateUserResponseModel> updateUser(@PathVariable String userId, @RequestBody CreateUserRequestModel requestModel){
        CreateUserResponseModel createUserResponseModel= usersService.updateUser(userId, requestModel);
        return new ResponseEntity<>(createUserResponseModel, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('profile:delete') or principal == @emailSearch.getEmailFromId(#userId)")
    @DeleteMapping(path="/{userId}")
    public String deleteUser(@PathVariable String userId){
        return usersService.deleteUser(userId) ? "User deleted" : "User does not exist in db";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLogin authenticationRequest) throws Exception
    {
        Authentication authentication = authenticate(authenticationRequest.getEmail(),
        authenticationRequest.getPassword());
        final UserDetails userDetails =
                usersService.loadUserByUsername(authenticationRequest.getEmail());

        UserEntity userEntity = usersService.findByEmail(authenticationRequest.getEmail());
        CreateUserResponseModel userResponseModel = new ModelMapper()
                .map(userEntity, CreateUserResponseModel.class);

        final String token = jwtTokenUtil.generateToken(userDetails, authentication, userEntity.getId());
        return ResponseEntity.ok(new JwtResponse<>(userResponseModel, token));
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
