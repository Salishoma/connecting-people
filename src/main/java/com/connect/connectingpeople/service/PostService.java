package com.connect.connectingpeople.service;

import com.connect.connectingpeople.model.Post;
import com.connect.connectingpeople.ui.model.PostResponseModel;

import java.util.List;

public interface PostService {
    PostResponseModel createPost(Post post, String username);
    List<PostResponseModel> getAllPosts();
    PostResponseModel getPost(String postId);
    PostResponseModel editPost(String postId, String username, Post post);
    boolean deletePost(String postId, String userId);
}
