package com.example.gcs_demo.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private String objectName;
    private String link;
    private String gcsLink;
    private String contentType;
    private String publicLink;
}
