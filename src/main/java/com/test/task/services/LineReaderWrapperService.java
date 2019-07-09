package com.test.task.services;

import java.io.BufferedReader;
import java.io.LineNumberReader;

public interface LineReaderWrapperService {

    LineNumberReader getReader(String fileName);

    String readLine(BufferedReader reader);

    void close(LineNumberReader reader);

}
