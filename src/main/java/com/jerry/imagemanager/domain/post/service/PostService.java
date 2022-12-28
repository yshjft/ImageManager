package com.jerry.imagemanager.domain.post.service;

import com.jerry.imagemanager.domain.post.dto.PostCreateRequest;
import com.jerry.imagemanager.global.error.exception.InvalidRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.jerry.imagemanager.global.error.ErrorCode.INVALID_FILE_EXTENSION;
import static com.jerry.imagemanager.global.error.ErrorCode.INVALID_IMAGE_COUNT;

public class PostService {
    private static final int POST_IMAGE_MIN_COUNT = 1;
    private static final int POST_IMAGE_MAX_COUNT = 3;
    private static final String[] ALLOWED_FILE_TYPES = {"jpg", "jpeg", "png"};
    public void createPost(PostCreateRequest postCreateRequest, List<MultipartFile> files) {
        // 검증
        validateFileCount(files);
        files.forEach(this::validateFileType);

        // 게시물 저장
        //
    }

    private void validateFileCount(List<MultipartFile> files) {
        if(files == null || files.size() < POST_IMAGE_MIN_COUNT || files.size() > POST_IMAGE_MAX_COUNT) {
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
