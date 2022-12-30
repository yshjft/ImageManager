package com.jerry.imagemanager.global.common.service;

import com.jerry.imagemanager.global.config.AWSS3MockConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(AWSS3MockConfig.class)
@SpringBootTest
class S3UploaderTest {
    @Autowired
    private S3Mock s3Mock;

    @Autowired
    private AwsS3Uploader awsS3Uploader;

    @AfterEach
    public void cleanUp() {
        s3Mock.stop();
    }

    @Test
    @DisplayName("s3 단일 파일 업로드 테스트")
    void singleFileUploadTest() throws IOException {
        // given
        String name = "test";
        String originalFileName = "test.png";
        String contentType = "image/png";
        String dirName = "test";

        MockMultipartFile file = new MockMultipartFile(name, originalFileName, contentType, "test".getBytes());

        // when
        String urlPath = awsS3Uploader.upload(file, dirName);

        // then
        assertThat(urlPath).contains(originalFileName);
    }

    @Test
    @DisplayName("s3 멀티 파일 업로드 테스트")
    void multiFileUploadTest() throws IOException {
        String contentType = "image/png";
        String dirName = "test";

        String name1 = "test1";
        String originalFileName1 = "test1.png";
        String name2 = "test2";
        String originalFileName2 = "test2.png";


        List<MultipartFile> files = List.of(
                new MockMultipartFile(name1, originalFileName1, contentType, name1.getBytes()),
                new MockMultipartFile(name2, originalFileName2, contentType, name2.getBytes())
        );

        List<String> urlPaths = awsS3Uploader.upload(files, dirName);

        assertThat(urlPaths.size()).isEqualTo(2);
        assertThat(urlPaths.get(0)).contains(originalFileName1);
        assertThat(urlPaths.get(1)).contains(originalFileName2);
    }
}