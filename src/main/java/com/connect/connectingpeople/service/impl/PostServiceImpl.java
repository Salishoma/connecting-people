package com.connect.connectingpeople.service.impl;

import com.connect.connectingpeople.model.Post;
import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.repository.PostRepository;
import com.connect.connectingpeople.repository.UsersRepository;
import com.connect.connectingpeople.service.PostService;
import com.connect.connectingpeople.ui.model.PostResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.connect.connectingpeople.enums.ApplicationUserRole.ADMIN;

@Service
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    UsersRepository usersRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UsersRepository usersRepository) {
        this.postRepository = postRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public PostResponseModel createPost(Post post, String username) {
        UserEntity user = usersRepository.findByEmail(username);
        if(user != null){
            post.setDate(new Date());
            Set<Post> posts = user.getPosts();
            post.setFullName(user.getFirstName() + " " + user.getLastName());
            posts.add(post);
            ModelMapper mapper = new ModelMapper();
            PostResponseModel postResponseModel = mapper.map(post, PostResponseModel.class);
            user.setPosts(posts);
            post.setUser(user);
            postRepository.save(post);
            return postResponseModel;
        }
        return null;
    }

    @Override
    public List<PostResponseModel> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new ModelMapper()
                        .map(post, PostResponseModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostResponseModel getPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElse(null);
        if(post != null){
            ModelMapper mapper = new ModelMapper();
            return mapper.map(post, PostResponseModel.class);
        }
        return null;
    }

    @Override
    public PostResponseModel editPost(String postId, String username, Post newPost) {
        UserEntity user = usersRepository.findById(username)
                .orElse(null);
        Post post = postRepository.findById(postId)
                .orElse(null);

        if(user != null && post != null && hasPermission(user, postId)){
            newPost.setDate(new Date());
            Set<Post> posts = user.getPosts();
            post.setPost(newPost.getPost());
            posts.add(post);
            postRepository.save(post);
            ModelMapper mapper = new ModelMapper();
            return mapper.map(post, PostResponseModel.class);
        }
        return null;
    }

    @Transactional
    @Override
    public boolean deletePost(String postId, String userId) {
        UserEntity user = usersRepository.findById(userId)
                .orElse(null);
        if(user != null && hasPermission(user, postId)){
            user.getPosts().remove(postRepository.findById(postId).orElse(null));
            usersRepository.save(user);
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }

    private boolean hasPermission(UserEntity user, String postId){
        return user.getRole().equals(ADMIN)  || user.getPosts().stream()
                .anyMatch(post -> post.getPostId().equals(postId));
    }
}
