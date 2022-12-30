package com.jerry.imagemanager.domain.post.controller;

import com.jerry.imagemanager.domain.post.dto.PostCreateRequest;
import com.jerry.imagemanager.domain.post.service.PostService;
import com.jerry.imagemanager.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPost(
            @ModelAttribute @Valid PostCreateRequest postCreateRequest,
            @RequestParam("files") List<MultipartFile> files
    ) {
       Long createdPostId = postService.createPost(postCreateRequest, files);

        return ResponseEntity.created(URI.create("/posts/"+createdPostId))
                .body(new ApiResponse<>("게시물 생성 완료", HttpStatus.CREATED.value()));
    }
}
