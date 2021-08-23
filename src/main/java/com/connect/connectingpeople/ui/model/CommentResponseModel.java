package com.connect.connectingpeople.ui.model;

import com.connect.connectingpeople.model.Likes;
import com.connect.connectingpeople.model.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
public class CommentResponseModel {
    private String commentId;

    private String comment;

    private Set<Likes> likes;

    private Post post;

    private String fullName;

    private String userId;

    private Date date;

}
