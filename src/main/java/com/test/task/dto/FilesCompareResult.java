package com.test.task.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FilesCompareResult {

    private final Set<String> firstFileOnlyLines = new HashSet<>();
    private final Set<String> secondFileOnlyLines = new HashSet<>();
    private final Map<String, Set<FileTuple>> firstFileMissedTuples = new HashMap<>();
    private final Map<String, Set<FileTuple>> secondFileMissedTuples = new HashMap<>();

    public Set<String> getFirstFileOnlyLines() {
        return firstFileOnlyLines;
    }

    public Set<String> getSecondFileOnlyLines() {
        return secondFileOnlyLines;
    }

    public Map<String, Set<FileTuple>> getFirstFileMissedTuples() {
        return firstFileMissedTuples;
    }

    public Map<String, Set<FileTuple>> getSecondFileMissedTuples() {
        return secondFileMissedTuples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilesCompareResult that = (FilesCompareResult) o;
        return Objects.equals(firstFileOnlyLines, that.firstFileOnlyLines) &&
                Objects.equals(secondFileOnlyLines, that.secondFileOnlyLines) &&
                Objects.equals(firstFileMissedTuples, that.firstFileMissedTuples) &&
                Objects.equals(secondFileMissedTuples, that.secondFileMissedTuples);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstFileOnlyLines, secondFileOnlyLines, firstFileMissedTuples, secondFileMissedTuples);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UUIDs only from first file:\n");

        firstFileOnlyLines.forEach(uuid -> stringBuilder.append(uuid).append(";\n"));

        stringBuilder.append("UUIDs only from first file:\n");
        secondFileOnlyLines.forEach(uuid -> stringBuilder.append(uuid).append(";\n"));

        stringBuilder.append("Unique tuples from first file:\n");
        firstFileMissedTuples.forEach((uuid, tuples) -> stringBuilder
                .append("UUID = ").append(uuid)
                .append(": ").append(tuples).append("\n"));

        stringBuilder.append("Unique tuples from second file:\n");
        secondFileMissedTuples.forEach((uuid, tuples) -> stringBuilder.append("UUID = ").append(uuid)
                .append(": ").append(tuples).append("\n"));

        return stringBuilder.toString();
    }
}
