package com.connect.connectingpeople.controller;

import com.connect.connectingpeople.model.Comment;
import com.connect.connectingpeople.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class CommentController {

    CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(path="{postId}/comments",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
            )
    public ResponseEntity<Comment> createNewComment(@PathVariable String postId, @RequestBody Comment comment, Principal principal){
        String userId = principal.getName();
        Comment newComment = commentService.createComment(comment, userId, postId);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @GetMapping(path="/comments/{commentId}",produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Comment> getComment(@PathVariable String commentId){
        Comment newComment = commentService.findCommentById(commentId);
        return new ResponseEntity<>(newComment, HttpStatus.OK);
    }

    @PutMapping(path="{postId}/comments/{commentId}",produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Comment> editComment(@PathVariable String commentId, @PathVariable String postId, @RequestBody Comment newComment, Principal principal){
        String userId = principal.getName();
        Comment comment = commentService.editComment(newComment, commentId, postId, userId);
        if(comment == null){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping(path = "{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId, @PathVariable String postId, Principal principal){
        String userId = principal.getName();
        boolean deleted = commentService.deleteComment(commentId, postId, userId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
