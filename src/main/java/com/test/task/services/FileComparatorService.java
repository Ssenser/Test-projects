package com.test.task.services;

import com.test.task.dto.FilesCompareResult;

public interface FileComparatorService {

    FilesCompareResult compareFiles(String fileName1, String fileName2);
}
