package com.jerry.imagemanager.domain.post.controller;

import com.jerry.imagemanager.domain.post.dto.PostCreateRequest;
import com.jerry.imagemanager.global.common.dto.ApiResponse;
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
public class PostController {
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPost(
            @ModelAttribute @Valid PostCreateRequest postCreateRequest,
            @RequestParam("files") List<MultipartFile> files
    ) {
        log.info("size : {}", files.size());

        return ResponseEntity.created(URI.create("/posts/"))
                .body(new ApiResponse<>(
                        "게시물 생성 완료",
                        HttpStatus.CREATED.value(),
                        ""));
    }
}
