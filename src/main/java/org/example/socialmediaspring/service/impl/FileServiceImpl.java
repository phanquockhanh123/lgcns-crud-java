package org.example.socialmediaspring.service.impl;

import org.example.socialmediaspring.entity.Files;
import org.example.socialmediaspring.entity.Product;
import org.example.socialmediaspring.repository.FileRepository;
import org.example.socialmediaspring.service.FilesService;
import org.example.socialmediaspring.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FilesService {
    private static final Logger log = LoggerFactory.getLogger(FilesService.class);

    @Autowired
    private FileRepository fileRepository;

    private final String FILE_PATH = "C:\\Users\\63200332\\Downloads\\lgcns\\lgcns-crud-java\\src\\main\\resources\\static\\public\\";

    @Override
    public String storeFile(MultipartFile file, Product product) throws IOException {
        Files files = Files
                .builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .product(product).build();

        log.info("data file bytes {} ", file.getBytes());

        files = fileRepository.save(files);

        log.info("File uploaded successfully into database with id {}", files.getId());

        if (files.getId() != null) {
            return "File uploaded successfully into database";
        }

        return null;
    }

    public byte[] getFiles(String fileName) {
        return ImageUtils.decompressImage(fileRepository.findByName(fileName).getImageData());
    }

    public String storeDataIntoFileSystem(MultipartFile file, Product product) throws IOException {
        String filePath = FILE_PATH + file.getOriginalFilename();

        Files files = Files.builder().name(file.getOriginalFilename()).path(filePath).type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).product(product).build();

        files = fileRepository.save(files);

        file.transferTo(new File(filePath));

        if (files.getId() != null) {
            return "File uploaded successfully into database";
        }

        return null;
    }

    public byte[] downloadFilesFromFileSystem(String fileName) throws IOException {
        String path = fileRepository.findByName(fileName).getPath();

        return java.nio.file.Files.readAllBytes(new File(path).toPath());
    }
}
