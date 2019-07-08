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

    private static final Integer MARK_AHEAD_CHARS = 1000000;

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
    public void mark(LineNumberReader reader) {
        try {
            reader.mark(MARK_AHEAD_CHARS);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Marking reader exception: ", ioe);
        }
    }

    @Override
    public void skipLines(LineNumberReader reader, int amount) {
        try {
            int i = 0;
            while (i < amount) {
                final String str = reader.readLine();
                if (str != null) {
                    i++;
                } else {
                    i = amount;
                }
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Skipping line exception: ", ioe);
        }
    }

    @Override
    public void reset(LineNumberReader reader) {
        try {
            reader.reset();
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Resting reader exception: ", ioe);
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
