package com.connect.connectingpeople.repository;

import com.connect.connectingpeople.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes, String> {
}
