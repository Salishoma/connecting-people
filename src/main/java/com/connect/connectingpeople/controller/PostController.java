package com.connect.connectingpeople.controller;

import com.connect.connectingpeople.model.Post;
import com.connect.connectingpeople.service.PostService;
import com.connect.connectingpeople.ui.model.PostResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(path="",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
            )
    public ResponseEntity<PostResponseModel> createPost(@Valid @RequestBody Post post, Principal principal){
        String username = principal.getName();
        PostResponseModel postResponseModel = postService.createPost(post, username);
        return new ResponseEntity<>(postResponseModel, HttpStatus.CREATED);
    }

    @GetMapping(path="/", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<PostResponseModel>> getAllPosts(){
        List<PostResponseModel> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(path="/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PostResponseModel> getPost(@PathVariable String postId){
        PostResponseModel post = postService.getPost(postId);
        if(post == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }


    @PutMapping(path="/{postId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PostResponseModel> updatePost(@PathVariable String postId, @RequestBody Post newPost, Principal principal){
        String username = principal.getName();
        PostResponseModel post = postService.editPost(postId, username, newPost);
        if(post == null){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping(path="/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId, Principal principal){
        String userId = principal.getName();
        boolean hasPermission = postService.deletePost(postId, userId);
        return hasPermission ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
