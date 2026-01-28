package com.oexil.univote.service;

import com.oexil.univote.utils.FileUtilizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@Service
public class ImageUploadService {

    @Value("${archive.path}")
    private String archivePath;

    private final String urlPrefix = "/files/";

    public String[] getResultsOfFileWrite(MultipartFile imageFile) {
        String fileName = new FileUtilizer().generateFileName(imageFile.getOriginalFilename());

        if (!new FileUtilizer().writeToDisk(imageFile, Paths.get(archivePath), fileName)) {
            return null;
        } else {
            // [Physical Path, URL Path, File Name]
            // Example URL: /files/12345.jpg
            return new String[]{(archivePath + "/" + fileName), (urlPrefix + fileName), (fileName)};
        }
    }
}