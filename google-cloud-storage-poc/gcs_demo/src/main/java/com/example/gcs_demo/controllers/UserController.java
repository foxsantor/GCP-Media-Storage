package com.example.gcs_demo.controllers;

import com.example.gcs_demo.common.Constants;
import com.example.gcs_demo.models.User;
import com.example.gcs_demo.services.GoogleCloudService;
import com.example.gcs_demo.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/test")
@Slf4j
public class UserController {

    private final UserService userService;
    private final GoogleCloudService googleCloudService;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> insertUser(@RequestParam(name = "user") String user, @RequestParam(required = false, name = "file") MultipartFile file) {
        User userObject = new User();
        Blob fileHolder;
        try {
            log.info("Uploading to GCS ...");
            /*Since FORM-DATA doesn't allow for json to bes sent
             * I had to send the user's info in one form which forced the data to go in as a String
             * and then we use OBJECT MAPPER To get hold of the JSON again
             * */
            userObject = new ObjectMapper().readValue(user, User.class);
            //IMPORTANT ATTRIBUTE objectName more like an id in the bucket
            String objectName = userObject.getUsername() + UUID.randomUUID();
            userObject.setObjectName(objectName);
            //setting the public url for retrieving the file
            userObject.setPublicLink(Constants.PUBLIC_GOOGLE_CLOUD_URL + Constants.BUCKET_NAME + "/" + objectName);
            //checking if we have an upload via link or file
            if (!userObject.getLink().isEmpty() && file == null) {
                fileHolder = googleCloudService.uploadFileViaLink(userObject.getLink(), objectName);
            } else {
                log.debug("file received [{}] ", file);
                fileHolder = googleCloudService.uploadFile(file, objectName);
            }
            //setting the direct download link from GCS
            userObject.setGcsLink(fileHolder.getMediaLink());
            //setting the content type
            userObject.setContentType(fileHolder.getContentType());
            log.debug("user info [{}]", userObject);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(userService.insertUser(userObject));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.deleteUser(id));

    }

    @GetMapping(value = "/data/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> getData(@PathVariable String id) {
        User user = userService.getUserData(id);
        return ResponseEntity.ok(user);
    }
}
