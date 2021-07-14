package com.connect.connectingpeople.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String postId;

    private String post;
    private Date date;

    @OneToMany
    private List<Comment> comment;

    @OneToMany
    private List<Likes> like;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId")
    UserEntity user;
}
