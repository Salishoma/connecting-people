package com.connect.connectingpeople.config;

import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.connect.connectingpeople.enums.ApplicationUserRole.ADMIN;

@Configuration
public class AdminConfig {


    PasswordEncoder passwordEncoder;
    UsersRepository usersRepository;

    @Autowired
    public AdminConfig(PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    @Bean
    public void createAdmin(){
        UserEntity user = new UserEntity();
        user.setId("1");
        user.setFirstName("Abubakar");
        user.setLastName("Salifu");
        user.setEmail("abubakar@gmail.com");
        user.setEncryptedPassword(passwordEncoder.encode("abcdefghij"));
        user.setRole(ADMIN);
        usersRepository.save(user);
    }
}
