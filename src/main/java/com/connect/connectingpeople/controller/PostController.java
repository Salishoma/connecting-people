package com.connect.connectingpeople.controller;

import com.connect.connectingpeople.model.Post;
import com.connect.connectingpeople.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(path="/posts",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
            )
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post, Principal principal){
        String userId = principal.getName();
        Post newPost = postService.createPost(post, userId);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping(path="/posts", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(path="/posts/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Post> getPost(@PathVariable String postId){
        Post post = postService.getPost(postId);
        if(post == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping(path="/posts/{postId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Post> updatePost(@PathVariable String postId, @RequestBody Post newPost, Principal principal){
        String userId = principal.getName();
        Post post = postService.editPost(postId, userId, newPost);
        if(post == null){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping(path="/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId, Principal principal){
        String userId = principal.getName();
        boolean hasPermission = postService.deletePost(postId, userId);
        return hasPermission ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
