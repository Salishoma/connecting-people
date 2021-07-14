package com.connect.connectingpeople.controller;

import com.connect.connectingpeople.model.Post;
import com.connect.connectingpeople.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class PostController {

    PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create-new-post")
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post, Principal principal){
        String userId = principal.getName();
        Post newPost = postService.createPost(post, userId);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping("/feeds")
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable String postId){
        Post post = postService.getPost(postId);
        if(post == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable String postId, @RequestBody Post newPost, Principal principal){
        String userId = principal.getName();
        Post post = postService.editPost(postId, userId, newPost);
        if(post == null){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping(path="/post/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId, Principal principal){
        String userId = principal.getName();
        boolean hasPermission = postService.deletePost(postId, userId);
        return hasPermission ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
