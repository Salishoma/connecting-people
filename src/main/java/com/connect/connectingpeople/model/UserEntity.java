package com.connect.connectingpeople.model;

import com.connect.connectingpeople.enums.ApplicationUserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="user")
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

    @JsonManagedReference(value="user-reference")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private Set<Post> posts;

    private ApplicationUserRole role;
}
