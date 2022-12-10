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

import java.io.IOException;

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
    @DisplayName("s3 이미지 업로드 테스트")
    void uploadTest() throws IOException {
        // given
        String path = "test.png";
        String contentTYp = "image/png";
        String dirName = "test";

        MockMultipartFile file = new MockMultipartFile("test", path, contentTYp, "test".getBytes());

        // when
        String urlPath = awsS3Uploader.upload(file, dirName);

        // then
        assertThat(urlPath).contains(path);
        assertThat(urlPath).contains(dirName);
    }
}