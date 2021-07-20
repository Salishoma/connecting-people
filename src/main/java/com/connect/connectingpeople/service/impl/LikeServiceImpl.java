package com.connect.connectingpeople.service.impl;

import com.connect.connectingpeople.model.Comment;
import com.connect.connectingpeople.model.Post;
import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.repository.CommentRepository;
import com.connect.connectingpeople.repository.LikeRepository;
import com.connect.connectingpeople.repository.PostRepository;
import com.connect.connectingpeople.repository.UsersRepository;
import com.connect.connectingpeople.service.LikeService;
import com.connect.connectingpeople.utils.LikesUtil;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    LikeRepository likeRepository;
    PostRepository postRepository;
    CommentRepository commentRepository;
    UsersRepository usersRepository;

    public LikeServiceImpl(LikeRepository likeRepository,
                           PostRepository postRepository,
                           CommentRepository commentRepository,
                           UsersRepository usersRepository
    ) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean likesPost(String userId, String postId) {
        UserEntity user = usersRepository.findById(userId).orElse(null);
        Post post = postRepository.findById(postId).orElse(null);

        LikesUtil<Post, PostRepository> likesUtil = new LikesUtil<>(likeRepository);
        return likesUtil.likes(user, post, postRepository);
    }

    @Override
    public boolean likesComment(String userId, String commentId) {
        UserEntity user = usersRepository.findById(userId).orElse(null);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        LikesUtil<Comment, CommentRepository> likesUtil = new LikesUtil<>(likeRepository);
        return likesUtil.likes(user, comment, commentRepository);
    }
}
