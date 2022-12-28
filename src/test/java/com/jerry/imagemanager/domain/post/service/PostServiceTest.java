package com.jerry.imagemanager.domain.post.service;

import com.jerry.imagemanager.domain.post.dto.PostCreateRequest;
import com.jerry.imagemanager.global.error.exception.InvalidRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Stream;

import static com.jerry.imagemanager.global.error.ErrorCode.INVALID_FILE_EXTENSION;
import static com.jerry.imagemanager.global.error.ErrorCode.INVALID_IMAGE_COUNT;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Nested
    @DisplayName("게시물 생성 테스트")
    @TestInstance(PER_CLASS)
    class CreatePostTest {

        @ParameterizedTest
        @MethodSource("fileCountFailParameter")
        @DisplayName("파일 개수 미충족 테스트")
        void testFileCountFailCase(List<MultipartFile> files) {
            assertThatThrownBy(() -> postService.createPost(new PostCreateRequest("제목"), files))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessage(INVALID_IMAGE_COUNT.getMessage());
        }

        @Test
        @DisplayName("파일 타입 미충족 테스트")
        void testFileTypeFailCase() {
            assertThatThrownBy(() -> postService.createPost(new PostCreateRequest("제목"), List.of(
                    new MockMultipartFile(
                            "file1",
                            "file1.pn",
                            MediaType.MULTIPART_FORM_DATA_VALUE,
                            "file1".getBytes()),
                    new MockMultipartFile(
                            "file2",
                            "file2.png",
                            MediaType.MULTIPART_FORM_DATA_VALUE,
                            "file2".getBytes()),
                    new MockMultipartFile(
                            "file3",
                            "file3.png",
                            MediaType.MULTIPART_FORM_DATA_VALUE,
                            "file3".getBytes()))
            ))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessage(INVALID_FILE_EXTENSION.getMessage());

        }

        private Stream<Arguments> fileCountFailParameter() {
            return Stream.of(null, Arguments.of(List.of()), Arguments.of(List.of(
                    new MockMultipartFile(
                            "file1",
                            "file1.png",
                            MediaType.MULTIPART_FORM_DATA_VALUE,
                            "file1".getBytes()),
                    new MockMultipartFile(
                            "file2",
                            "file2.png",
                            MediaType.MULTIPART_FORM_DATA_VALUE,
                            "file2".getBytes()),
                    new MockMultipartFile(
                            "file3",
                            "file3.png",
                            MediaType.MULTIPART_FORM_DATA_VALUE,
                            "file3".getBytes()),
                    new MockMultipartFile(
                            "file4",
                            "file4.png",
                            MediaType.MULTIPART_FORM_DATA_VALUE,
                            "file4".getBytes())
            )));
        }
    }
}