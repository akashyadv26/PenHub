package com.PenHub.PenHub.services;

import jakarta.annotation.PostConstruct;
import org.aspectj.bridge.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class ImageService {

    private static final String BASE_URL="http://localhost:8081/dev/api/PenHub/";
    private static final String IMAGE_DIRECTORY="src/main/resources/static/images/posts";
    private static final String IMAGE_URL="images/posts/";


    @PostConstruct
    public void init(){
        Path directorypath= Paths.get(IMAGE_DIRECTORY);
        if (Files.notExists(directorypath)){
            try {
                Files.createDirectories(directorypath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String saveImage(MultipartFile image) throws IOException, NoSuchAlgorithmException {
        String uniqueFileName=generateUniqueFileName(image.getOriginalFilename());
        Path filePath=Paths.get(IMAGE_DIRECTORY,uniqueFileName);
        Files.write(filePath,image.getBytes());
        String finalUrl=BASE_URL+IMAGE_URL+uniqueFileName;
        return finalUrl;

    }

    private String generateUniqueFileName(String originalFileName) throws NoSuchAlgorithmException {
        String timeStamp=String.valueOf(System.currentTimeMillis());
        MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte [] hash= digest.digest((originalFileName+timeStamp).getBytes());
        return Base64.getUrlEncoder().encodeToString(hash)+getFileExtension(originalFileName);

    }

    private String getFileExtension(String fileName){
        return fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }
}
