package com.jerry.imagemanager.global.common.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jerry.imagemanager.global.error.exception.FileConvertingFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jerry.imagemanager.global.error.ErrorCode.FILE_CONVERTING_FAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) {
        try {
            File uploadFile = convert(multipartFile).orElseThrow(() -> new FileConvertingFailException(FILE_CONVERTING_FAIL));
            return upload(uploadFile, dirName);
        }catch (IOException ioException) {
            throw new FileConvertingFailException(FILE_CONVERTING_FAIL);
        }
    }

    public List<String> upload(List<MultipartFile> multipartFiles, String dirName) {
            return multipartFiles.stream()
                    .map(multipartFile -> {
                        try {
                            File uploadFile = convert(multipartFile).orElseThrow(() -> new FileConvertingFailException(FILE_CONVERTING_FAIL));
                            return upload(uploadFile, dirName);
                        }catch (IOException ioException) {
                            throw new FileConvertingFailException(FILE_CONVERTING_FAIL);
                        }
                    })
                    .collect(Collectors.toList());
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        // ????????? ????????? File ??????
        removeNewFile(uploadFile);

        // ???????????? ????????? S3 URL ?????? ??????
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        // PublicRead ???????????? ?????????
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
             log.info("????????? ?????????????????????.");
        }else {
             log.warn("????????? ???????????? ???????????????.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException{
        File convertFile = new File(file.getOriginalFilename());

        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
