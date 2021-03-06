package com.connect.connectingpeople.repository;

import com.connect.connectingpeople.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String username);
}
