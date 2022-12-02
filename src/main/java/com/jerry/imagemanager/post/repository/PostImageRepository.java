package com.jerry.imagemanager.post.repository;

import com.jerry.imagemanager.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
