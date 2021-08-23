package com.connect.connectingpeople.ui.model;

import com.connect.connectingpeople.model.Comment;
import com.connect.connectingpeople.model.Likes;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
public class PostResponseModel {
    private String postId;

    private String post;

    private Date date;

    private Set<CommentResponseModel> comments;

    private Set<Likes> likes;

    private String fullName;

    private String userId;
}
