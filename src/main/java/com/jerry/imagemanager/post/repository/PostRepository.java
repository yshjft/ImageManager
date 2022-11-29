package com.jerry.imagemanager.post.repository;

import com.jerry.imagemanager.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
}
