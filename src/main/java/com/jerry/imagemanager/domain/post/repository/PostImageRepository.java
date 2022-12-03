package com.jerry.imagemanager.domain.post.repository;

import com.jerry.imagemanager.domain.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
