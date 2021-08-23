package com.connect.connectingpeople.model;

import com.connect.connectingpeople.utils.ModelUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="posts")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"Likes"})
public class Post extends ModelUtil {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(length = 60, updatable = false, nullable = false)
    private String postId;

    @Column(length = 250)
    private String post;

    private Date date;

    @JsonManagedReference(value="post-reference")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post", orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Likes> likes;

    @JsonBackReference(value="user-reference")
    @ManyToOne
    @JoinColumn(name = "userId")
    UserEntity user;

    @Column(length = 60)
    private String fullName;

    @Override
    public Set<Likes> getLikes() {
        return likes;
    }
}
