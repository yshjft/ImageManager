package com.jerry.imagemanager.domain.post.service;

import com.jerry.imagemanager.domain.post.Post;
import com.jerry.imagemanager.domain.post.PostImage;
import com.jerry.imagemanager.domain.post.dto.PostCreateRequest;
import com.jerry.imagemanager.domain.post.repository.PostImageRepository;
import com.jerry.imagemanager.domain.post.repository.PostRepository;
import com.jerry.imagemanager.global.common.service.AwsS3Uploader;
import com.jerry.imagemanager.global.error.exception.FileConvertingFailException;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Stream;

import static com.jerry.imagemanager.global.error.ErrorCode.INVALID_FILE_EXTENSION;
import static com.jerry.imagemanager.global.error.ErrorCode.INVALID_IMAGE_COUNT;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private PostImageRepository postImageRepository;

    @Mock
    private AwsS3Uploader awsS3Uploader;

    @InjectMocks
    private PostService postService;

    @Nested
    @DisplayName("게시물 생성 테스트")
    class CreatePostTest {
        @Nested
        @DisplayName("실패 케이스 테스트")
        class FailCaseTest {
            @Nested
            @DisplayName("파일 개수 미충족 테스트")
            @TestInstance(PER_CLASS)
            class FileCountFailTest {
                @ParameterizedTest
                @MethodSource("fileCountFailParameter")
                void testFileCountFailCase(List<MultipartFile> files) {
                    assertThatThrownBy(() -> postService.createPost(new PostCreateRequest("제목"), files))
                            .isInstanceOf(InvalidRequestException.class)
                            .hasMessage(INVALID_IMAGE_COUNT.getMessage());
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

            @Test
            @DisplayName("파일 업로드 실패 테스트")
            void testFileUploadFailCase() {
                PostCreateRequest postCreateRequest = new PostCreateRequest("제목");
                List<MultipartFile> files = List.of(new MockMultipartFile(
                        "file1",
                        "file1.png",
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        "file1".getBytes()));
                Post post = new Post(postCreateRequest.getTitle());

                // when
                when(postRepository.save(any(Post.class))).thenReturn(post);
                when(awsS3Uploader.upload(files, "post")).thenThrow(FileConvertingFailException.class);

                // given
                 assertThatThrownBy(() -> postService.createPost(postCreateRequest, files))
                        .isInstanceOf(FileConvertingFailException.class);

                // then
                verify(postRepository).save(any(Post.class));
                verify(awsS3Uploader).upload(files, "post");
                verify(postImageRepository, never()).saveAll(any(List.class));
            }
        }

        @Nested
        @DisplayName("성공 케이스 테스트")
        class SuccessCaseTest {
            @Test
            @DisplayName("게시물 생성 성공 테스트")
            void testPostCreationSuccess() {
                PostCreateRequest postCreateRequest = new PostCreateRequest("제목");
                List<MultipartFile> files = List.of(new MockMultipartFile(
                        "file1",
                        "file1.png",
                        MediaType.MULTIPART_FORM_DATA_VALUE,
                        "file1".getBytes()));

                String imageUrl = "https://www.s3.com/post/file1.png";
                List<String> imageUrls = List.of(imageUrl);

                Post post = new Post(postCreateRequest.getTitle());
                List<PostImage> postImages = List.of(new PostImage(imageUrl, post));

                when(postRepository.save(any(Post.class))).thenReturn(post);
                when(awsS3Uploader.upload(files, "post")).thenReturn(imageUrls);
                when(postImageRepository.saveAll(any(List.class))).thenReturn(postImages);

                postService.createPost(postCreateRequest, files);

                verify(postRepository).save(any(Post.class));
                verify(awsS3Uploader).upload(files, "post");
                verify(postImageRepository).saveAll(any(List.class));
            }
        }

    }
}