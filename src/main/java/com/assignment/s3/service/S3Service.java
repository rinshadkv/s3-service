package com.assignment.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.assignment.s3.Dto.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }


    public List<FileInfo> searchFilesInUserFolder(String userName, String searchTerm) {
        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(userName + "/");

        ListObjectsV2Result result = amazonS3.listObjectsV2(listObjectsRequest);
// this download only valid for 24 hours
        return result.getObjectSummaries().stream()
                .filter(summary -> summary.getKey().contains(searchTerm))
                .map(summary -> {
                    String fileKey = summary.getKey();
                    URL downloadUrl = generatePresignedUrl(fileKey);
                    return new FileInfo(fileKey, downloadUrl.toString());
                })
                .collect(Collectors.toList());
    }


    private URL generatePresignedUrl(String fileKey) {
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fileKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(java.sql.Date.valueOf(java.time.LocalDate.now().plusDays(1))); // URL valid for 1 day
        return amazonS3.generatePresignedUrl(urlRequest);
    }

    public void uploadFile(String userName, String fileName, InputStream fileInputStream) {
        String filePath = userName + "/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        amazonS3.putObject(new PutObjectRequest(bucketName, filePath, fileInputStream, metadata));
    }
}
