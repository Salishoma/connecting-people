package com.connect.connectingpeople.service;

import com.connect.connectingpeople.model.Post;

import java.util.List;

public interface PostService {
    Post createPost(Post post, String userId);
    List<Post> getAllPosts();
    Post getPost(String postId);
    Post editPost(String postId, String userId, Post post);
    boolean deletePost(String postId, String userId);
}
