package com.jerry.imagemanager.domain.post.repository;

import com.jerry.imagemanager.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
}
