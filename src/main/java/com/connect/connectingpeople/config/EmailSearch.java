package com.connect.connectingpeople.config;

import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailSearch {

    @Autowired
    UsersRepository repository;

    public String getEmailFromId(String userId){
        UserEntity entity = repository.findById(userId).orElse(null);
        if(entity != null){
            return entity.getEmail();
        }
        return null;
    }
}
