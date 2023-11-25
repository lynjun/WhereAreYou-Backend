package com.example.whereareyou.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;

    private final AmazonS3 amazonS3;

    private final AmazonS3Client amazonS3Client;

    @Autowired
    public AwsS3Service(AmazonS3 amazonS3, AmazonS3Client amazonS3Client) {
        this.amazonS3 = amazonS3;
        this.amazonS3Client = amazonS3Client;
    }

    public String upload(MultipartFile multipartFile) throws Exception {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("MultipartFile is null or empty");
        }

        String originalName = createFileName(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        ObjectMetadata objectMetaData = new ObjectMetadata();
        if (multipartFile.getOriginalFilename().toLowerCase().endsWith(".png")) {
            objectMetaData.setContentType("image/png");
        } else {
            objectMetaData.setContentType(multipartFile.getContentType());
        }
        objectMetaData.setContentLength(size);

        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return amazonS3Client.getUrl(S3Bucket, originalName).toString();
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(S3Bucket, fileName));
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}
