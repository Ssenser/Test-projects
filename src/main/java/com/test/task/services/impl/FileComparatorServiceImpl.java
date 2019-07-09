package com.test.task.services.impl;

import com.test.task.converter.Converter;
import com.test.task.dto.FileLine;
import com.test.task.dto.FileTuple;
import com.test.task.dto.FilesCompareResult;
import com.test.task.services.FileComparatorService;
import com.test.task.services.LineReaderWrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FileComparatorServiceImpl implements FileComparatorService {

    @Autowired
    private LineReaderWrapperService lineReaderWrapperService;

    @Autowired
    private Converter<String, FileLine> fileLineToDtoConverter;

    @Override
    public FilesCompareResult compareFiles(final String fileName1,
                                           final String fileName2) {
        final FilesCompareResult filesCompareResult = new FilesCompareResult();

        LineNumberReader firstFileReader = lineReaderWrapperService.getReader(fileName1);
        LineNumberReader secondFileReader = lineReaderWrapperService.getReader(fileName2);

        String firstFileLineStr = lineReaderWrapperService.readLine(firstFileReader);
        String secondFileLineStr = lineReaderWrapperService.readLine(secondFileReader);

        while (firstFileLineStr != null && secondFileLineStr != null) {
            final FileLine firstFileLine = fileLineToDtoConverter.convert(firstFileLineStr);
            final FileLine secondFileLine = fileLineToDtoConverter.convert(secondFileLineStr);

            if (firstFileLine.getUuid().equals(secondFileLine.getUuid())) {
                findAndStoreUniqueTuples(firstFileLine, secondFileLine, filesCompareResult);
                firstFileLineStr = lineReaderWrapperService.readLine(firstFileReader);
                secondFileLineStr = lineReaderWrapperService.readLine(secondFileReader);
            } else if (firstFileLine.getUuid().compareTo(secondFileLine.getUuid()) > 0) {
                filesCompareResult.getSecondFileOnlyUuids().add(secondFileLine.getUuid());
                secondFileLineStr = lineReaderWrapperService.readLine(secondFileReader);
            } else {
                filesCompareResult.getFirstFileOnlyUuids().add(firstFileLine.getUuid());
                firstFileLineStr = lineReaderWrapperService.readLine(firstFileReader);
            }
        }

        if (firstFileLineStr != null) {
            final List<String> uuids = writeRemainingToUnique(firstFileReader, firstFileLineStr);
            filesCompareResult.getFirstFileOnlyUuids().addAll(uuids);
        } else if (secondFileLineStr != null) {
            final List<String> uuids = writeRemainingToUnique(secondFileReader, secondFileLineStr);
            filesCompareResult.getSecondFileOnlyUuids().addAll(uuids);
        }

        lineReaderWrapperService.close(firstFileReader);
        lineReaderWrapperService.close(secondFileReader);

        return filesCompareResult;
    }

    private void findAndStoreUniqueTuples(FileLine firstFileLine,
                                          FileLine secondFileLine,
                                          FilesCompareResult filesCompareResult) {
        Set<FileTuple> secondFileUniqueTuples = new HashSet<>(secondFileLine.getTuples());
        Set<FileTuple> firstFileUniqueTuples = new HashSet<>();

        for (FileTuple fileTuple : firstFileLine.getTuples()) {
            if (!secondFileUniqueTuples.add(fileTuple)) {
                secondFileUniqueTuples.remove(fileTuple);
            } else {
                secondFileUniqueTuples.remove(fileTuple);
                firstFileUniqueTuples.add(fileTuple);
            }
        }

        if (!secondFileUniqueTuples.isEmpty()) {
            filesCompareResult.getUuidToSecondFileUniqueTuples().put(secondFileLine.getUuid(), secondFileUniqueTuples);
        }

        if (!firstFileUniqueTuples.isEmpty()) {
            filesCompareResult.getUuidToFirstFileUniqueTuples().put(firstFileLine.getUuid(), firstFileUniqueTuples);
        }
    }

    private List<String> writeRemainingToUnique(LineNumberReader fileReader,
                                                String fileLineStr) {
        final List<String> uuids = new ArrayList<>();

        while (fileLineStr != null) {
            uuids.add(fileLineToDtoConverter.convert(fileLineStr).getUuid());
            fileLineStr = lineReaderWrapperService.readLine(fileReader);
        }

        return uuids;
    }
}
