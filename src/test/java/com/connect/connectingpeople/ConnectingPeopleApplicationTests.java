package com.connect.connectingpeople;

import com.connect.connectingpeople.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ConnectingPeopleApplicationTests {

    @Autowired
    private UsersRepository usersRepository;


    @Test
    void contextLoads() {
    }

}
