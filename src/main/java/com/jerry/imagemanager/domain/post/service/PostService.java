package com.jerry.imagemanager.domain.post.service;

import com.jerry.imagemanager.domain.post.Post;
import com.jerry.imagemanager.domain.post.PostImage;
import com.jerry.imagemanager.domain.post.dto.request.PostCreateRequest;
import com.jerry.imagemanager.domain.post.dto.response.ImageResponse;
import com.jerry.imagemanager.domain.post.dto.response.PostResponse;
import com.jerry.imagemanager.domain.post.repository.PostImageRepository;
import com.jerry.imagemanager.domain.post.repository.PostRepository;
import com.jerry.imagemanager.global.common.service.AwsS3Uploader;
import com.jerry.imagemanager.global.error.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jerry.imagemanager.global.error.ErrorCode.INVALID_FILE_EXTENSION;
import static com.jerry.imagemanager.global.error.ErrorCode.INVALID_IMAGE_COUNT;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final int POST_IMAGE_MIN_COUNT = 1;
    private static final int POST_IMAGE_MAX_COUNT = 3;
    private static final String[] ALLOWED_FILE_TYPES = {"jpg", "jpeg", "png"};

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest, List<MultipartFile> files) {
        // 검증
        validateFileCount(files);
        files.forEach(this::validateFileType);

        // 게시물 저장
        Post post = postRepository.save(new Post(postCreateRequest.getTitle()));

        List<PostImage> postImages = awsS3Uploader.upload(files, "post").stream()
                .map(imageUrl -> new PostImage(imageUrl, post))
                .collect(Collectors.toList());
        postImageRepository.saveAll(postImages);

        return post.getId();
    }

    // 조회
    @Transactional(readOnly = true)
    public Slice<PostResponse> getPosts(Pageable pageable) {
        Slice<Post> slicedPosts = postRepository.findPosts(pageable);

       return slicedPosts.map(post -> {
            List<ImageResponse> images = post.getPostImages().stream()
                    .map(postImage -> new ImageResponse(postImage.getImageUrl()))
                    .collect(Collectors.toList());

            return PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .images(images)
                    .build();
        });
    }

    private void validateFileCount(List<MultipartFile> files) {
        if(files == null || files.get(0).getSize() == 0 || files.size() < POST_IMAGE_MIN_COUNT || files.size() > POST_IMAGE_MAX_COUNT) {
            throw new InvalidRequestException(INVALID_IMAGE_COUNT);
        }
    }

    private void validateFileType(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String fileExtension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        if(Arrays.stream(ALLOWED_FILE_TYPES).noneMatch(fileType -> fileType.equals(fileExtension))) {
            throw new InvalidRequestException(INVALID_FILE_EXTENSION);
        }
    }
}
