package com.connect.connectingpeople.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="likes")
@Getter
@Setter
@NoArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(length = 60)
    private String likesId;

    @JsonBackReference(value="user-reference")
    @ManyToOne
    private UserEntity user;

    private Date date;
}
