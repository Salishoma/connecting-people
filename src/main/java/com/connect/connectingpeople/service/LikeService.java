package com.connect.connectingpeople.service;

public interface LikeService {
    boolean likesPost(String userId, String postId);

    boolean likesComment(String userId, String commentId);
}
