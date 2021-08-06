package com.connect.connectingpeople.model;

import com.connect.connectingpeople.utils.ModelUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="comments")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"Likes"})
public class Comment extends ModelUtil {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(length = 60)
    private String commentId;

    @Column(length = 250)
    private String comment;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Likes> likes;

    @JsonBackReference(value="post-reference")
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @JsonBackReference(value="user-reference")
    @ManyToOne
    UserEntity user;

    @Column(length = 60)
    private String fullName;

    private Date date;

    @Override
    public Set<Likes> getLikes() {
        return likes;
    }
}
