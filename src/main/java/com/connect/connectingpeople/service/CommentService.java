package com.connect.connectingpeople.service;

import com.connect.connectingpeople.model.Comment;
import com.connect.connectingpeople.ui.model.CommentResponseModel;

public interface CommentService {
    CommentResponseModel createComment(Comment comment, String userId, String postId);

    CommentResponseModel findCommentById(String commentId);

    CommentResponseModel editComment(Comment comment, String commentId, String postId, String userId);

    boolean deleteComment(String commentId, String postId, String userId);
}
