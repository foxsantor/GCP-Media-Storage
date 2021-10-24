package com.example.gcs_demo.services;

import com.example.gcs_demo.common.Constants;
import com.example.gcs_demo.models.User;
import com.example.gcs_demo.utils.ImageConverter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class GoogleCloudServiceImpl implements GoogleCloudService {

    private static StorageOptions storageOption;

    public GoogleCloudServiceImpl() {
        try {
            storageOption = StorageOptions.newBuilder()
                    .setProjectId(Constants.PROJECT_ID)
                    .setCredentials(GoogleCredentials.fromStream(new
                            FileInputStream(Constants.KEY_PATH_LOCATION))).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createBucket() {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        // Creates the new bucket
        Bucket bucket = storage.create(BucketInfo.of(Constants.BUCKET_NAME));
        log.info("Bucket [{}] has been created", bucket.getName());
    }

    @Override
    public Blob uploadFileViaLink(String link, String objectName) throws IOException {
        Storage storage = storageOption.getService();
        BlobId blobId = BlobId.of(Constants.BUCKET_NAME, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob blob = storage.create(blobInfo, ImageConverter.getImageBytesFromUrl(link));
        log.info(
                "File uploaded to bucket " + Constants.BUCKET_NAME + " as " + objectName);
        return blob;
    }

    @Override
    public Blob uploadFile(MultipartFile file, String objectName) throws IOException {
        Storage storage = storageOption.getService();
        BlobId blobId = BlobId.of(Constants.BUCKET_NAME, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        byte[] byteArr = file.getBytes();
        Blob blob = storage.create(blobInfo, byteArr);
        log.info(
                "File uploaded to bucket " + Constants.BUCKET_NAME + " as " + objectName);
        return blob;
    }

    @Override
    public Blob retrieveFile(User user) {
        Storage storage = storageOption.getService();
        Blob blob = storage.get(BlobId.of(Constants.BUCKET_NAME, user.getObjectName()));
        return blob;
    }

    @Override
    public Path downloadFile(User user) {
        Blob file = retrieveFile(user);
        Path path = Paths.get(Constants.DOWNLOAD_FOLDER + user.getObjectName() + ".jpg");
        file.downloadTo(path);
        log.info(
                "Downloaded object "
                        + user.getObjectName()
                        + " from bucket name "
                        + Constants.BUCKET_NAME
                        + " to "
                        + Constants.DOWNLOAD_FOLDER);
        return path;
    }

}
