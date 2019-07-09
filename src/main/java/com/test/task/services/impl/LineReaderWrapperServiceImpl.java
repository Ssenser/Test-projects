package com.test.task.services.impl;

import com.test.task.services.LineReaderWrapperService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

@Service
public class LineReaderWrapperServiceImpl implements LineReaderWrapperService {

    @Override
    public LineNumberReader getReader(String fileName) {
        try {
            return new LineNumberReader(new FileReader(fileName));
        } catch (FileNotFoundException nfe) {
            throw new IllegalArgumentException("File " + fileName + " wasn't found: ", nfe);
        }
    }

    @Override
    public String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Reading line exception: ", ioe);
        }
    }

    @Override
    public void close(LineNumberReader reader) {
        try {
            reader.close();
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Closing reader exception: ", ioe);
        }
    }
}
