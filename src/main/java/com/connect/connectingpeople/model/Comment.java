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
public class Comment {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String commentId;

    private String comment;

    @OneToMany
    private List<Likes> likes;

    @JsonBackReference(value="post-reference")
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @JsonBackReference(value="user-reference")
    @ManyToOne
    @JoinColumn(name = "userId")
    UserEntity user;

    private String fullName;

    private Date date;
}
