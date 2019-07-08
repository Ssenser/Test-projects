package com.test.task.services.impl;

import com.test.task.services.FileCreatorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileCreatorServiceImpl implements FileCreatorService {

    @Override
    public String createFile(String filePath, String content) {
        try {
            return Files.write(Paths.get(filePath), content.getBytes()).toString();
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Some error occurs while writing to file: ", ioe);
        }
    }
}
