package org.example.socialmediaspring.service;

import org.example.socialmediaspring.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FilesService {
    String storeFile(MultipartFile file, Product product) throws IOException;

    byte[] getFiles(String fileName);

    String storeDataIntoFileSystem(MultipartFile file, Product product) throws IOException;

    byte[] downloadFilesFromFileSystem(String fileName) throws  IOException;
}
