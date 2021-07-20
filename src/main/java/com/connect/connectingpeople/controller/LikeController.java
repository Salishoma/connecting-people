package com.connect.connectingpeople.controller;
import com.connect.connectingpeople.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LikeController {

    LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<String> likePost(@PathVariable String postId, Principal principal){
        String userId = principal.getName();
        boolean isValid = likeService.likesPost(userId, postId);
        return isValid ? new ResponseEntity<>("Valid request", HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<String> likeComment(@PathVariable String commentId, Principal principal){
        String userId = principal.getName();
        boolean isValid = likeService.likesComment(userId, commentId);
        return isValid ? new ResponseEntity<>("Valid request", HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
