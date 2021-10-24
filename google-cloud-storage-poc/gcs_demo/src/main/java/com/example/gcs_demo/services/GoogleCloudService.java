package com.example.gcs_demo.services;

import com.example.gcs_demo.models.User;
import com.google.cloud.storage.Blob;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;


public interface GoogleCloudService {

    void createBucket();

    Blob uploadFileViaLink(String link, String objectName) throws IOException;

    Blob uploadFile(MultipartFile filePath, String objectName) throws IOException;

    Blob retrieveFile(User user);

    Path downloadFile(User user);
}
