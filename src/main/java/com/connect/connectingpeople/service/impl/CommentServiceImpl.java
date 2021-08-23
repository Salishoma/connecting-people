package com.connect.connectingpeople.service.impl;

import com.connect.connectingpeople.model.Comment;
import com.connect.connectingpeople.model.Post;
import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.repository.CommentRepository;
import com.connect.connectingpeople.repository.PostRepository;
import com.connect.connectingpeople.repository.UsersRepository;
import com.connect.connectingpeople.service.CommentService;
import com.connect.connectingpeople.ui.model.CommentResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UsersRepository usersRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public CommentResponseModel createComment(Comment comment, String userId, String postId) {
        Post post = postRepository.findById(postId)
                .orElse(null);
        UserEntity user = usersRepository.findById(userId)
                .orElse(null);
        if(user != null && post != null){
            comment.setDate(new Date());
            Set<Comment> comments = post.getComments();
            comment.setFullName(user.getFirstName() + " " + user.getLastName());
            comment.setUser(user);
            comment.setPost(post);
            comments.add(comment);
            post.setComments(comments);
            return new ModelMapper()
                    .map(commentRepository.save(comment), CommentResponseModel.class);
        }
        return null;
    }

    @Override
    public CommentResponseModel findCommentById(String commentId) {
        return new ModelMapper()
                .map(commentRepository.findById(commentId)
                        .orElse(null), CommentResponseModel.class);
    }

    @Override
    public CommentResponseModel editComment(Comment newComment, String commentId, String postId, String userId) {
        UserEntity user = usersRepository.findById(userId)
                .orElse(null);
        Post post = postRepository.findById(postId)
                .orElse(null);
        Comment comment = commentRepository.findById(commentId)
                .orElse(null);
        if(user != null && post != null && comment != null && comment.getUser().getId().equals(userId)){
            Set<Comment> comments = post.getComments();
            comment.setDate(new Date());
            comment.setComment(newComment.getComment());
            comments.add(comment);
            commentRepository.save(comment);
            return new ModelMapper()
                    .map(commentRepository.save(comment), CommentResponseModel.class);
        }
        return null;
    }

    @Transactional
    @Override
    public boolean deleteComment(String commentId, String postId, String userId) {
        UserEntity user = usersRepository.findById(userId)
                .orElse(null);
        Post post = postRepository.findById(postId)
                .orElse(null);
        Comment comment = commentRepository.findById(commentId)
                .orElse(null);
        if(user != null && post != null && comment != null && comment.getUser().getId().equals(userId)){
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }
}
