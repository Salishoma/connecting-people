package com.connect.connectingpeople.utils;

import com.connect.connectingpeople.model.Likes;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.Set;

@MappedSuperclass
public class ModelUtil {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Likes> likes;

    public ModelUtil() {}

    public Set<Likes> getLikes() {
        return likes;
    }

    public void setLikes(Set<Likes> likes) {
        this.likes = likes;
    }
}
