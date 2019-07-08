package com.test.task.services;

import java.io.BufferedReader;
import java.io.LineNumberReader;

public interface LineReaderWrapperService {

    LineNumberReader getReader(String fileName);

    String readLine(BufferedReader reader);

    void mark(LineNumberReader reader);

    void skipLines(LineNumberReader reader, int amount);

    void reset(LineNumberReader reader);

    void close(LineNumberReader reader);

}
