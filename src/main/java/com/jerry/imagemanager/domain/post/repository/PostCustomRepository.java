package com.jerry.imagemanager.domain.post.repository;

import com.jerry.imagemanager.domain.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostCustomRepository {
    Slice<Post> findPosts(Pageable pageable);
}
