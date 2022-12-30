package com.jerry.imagemanager.domain.post.controller;

import com.jerry.imagemanager.domain.post.dto.request.PostCreateRequest;
import com.jerry.imagemanager.domain.post.dto.response.PostResponse;
import com.jerry.imagemanager.domain.post.service.PostService;
import com.jerry.imagemanager.global.common.dto.ApiResponse;
import com.jerry.imagemanager.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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

        return ResponseEntity
                .created(URI.create("/posts/"+createdPostId))
                .body(new ApiResponse<>("게시물 생성 완료", CREATED.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Slice<PostResponse>>>> getPosts(@PageableDefault(page = 0, size = 5) Pageable pageable) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message("게시물 조회 성공")
                .status(OK.value())
                .data(new PageResponse(postService.getPosts(pageable)))
                .build();

        return ResponseEntity
                .ok()
                .body(apiResponse);
    }
}
