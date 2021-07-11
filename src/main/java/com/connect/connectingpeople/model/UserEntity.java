package com.connect.connectingpeople.model;

import com.connect.connectingpeople.enums.ApplicationUserRole;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="users")
@Data
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 8388016639308402482L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable=false, unique=true)
    private String encryptedPassword;

    private ApplicationUserRole role;
}
