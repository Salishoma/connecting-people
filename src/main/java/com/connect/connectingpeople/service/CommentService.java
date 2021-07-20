package com.connect.connectingpeople.service;

import com.connect.connectingpeople.model.Comment;

public interface CommentService {
    Comment createComment(Comment comment, String userId, String postId);

    Comment findCommentById(String commentId);

    Comment editComment(Comment comment, String commentId, String postId, String userId);

    boolean deleteComment(String commentId, String postId, String userId);
}
