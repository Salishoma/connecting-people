package com.connect.connectingpeople.utils;

import com.connect.connectingpeople.model.Likes;
import com.connect.connectingpeople.model.UserEntity;
import com.connect.connectingpeople.repository.LikeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Data
@Service
public class LikesUtil<T extends ModelUtil, U extends JpaRepository<T, String>> {

    LikeRepository likeRepository;

    @Autowired
    public LikesUtil(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public boolean likes(UserEntity user, T body, U repository){
        if(user != null && body != null){
            Set<Likes> likes = body.getLikes();
            Likes like = likes.stream().filter(like1 -> like1.getUser().equals(user))
                    .findFirst().orElse(new Likes());

            if(like.getLikesId() != null){
                body.getLikes().remove(
                        likeRepository.findById(like.getLikesId()).orElse(null));
                repository.save(body);
                likeRepository.delete(like);
            }else{
                like.setDate(new Date());
                like.setUser(user);
                likes.add(like);
                body.setLikes(likes);
                likeRepository.save(like);
            }
            return true;
        }
        return false;
    }
}
