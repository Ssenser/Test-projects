package com.test.task.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FilesCompareResult {

    private final Set<String> firstFileOnlyUuids = new HashSet<>();
    private final Set<String> secondFileOnlyUuids = new HashSet<>();
    private final Map<String, Set<FileTuple>> uuidToFirstFileUniqueTuples = new HashMap<>();
    private final Map<String, Set<FileTuple>> uuidToSecondFileUniqueTuples = new HashMap<>();

    public Set<String> getFirstFileOnlyUuids() {
        return firstFileOnlyUuids;
    }

    public Set<String> getSecondFileOnlyUuids() {
        return secondFileOnlyUuids;
    }

    public Map<String, Set<FileTuple>> getUuidToFirstFileUniqueTuples() {
        return uuidToFirstFileUniqueTuples;
    }

    public Map<String, Set<FileTuple>> getUuidToSecondFileUniqueTuples() {
        return uuidToSecondFileUniqueTuples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilesCompareResult that = (FilesCompareResult) o;
        return Objects.equals(firstFileOnlyUuids, that.firstFileOnlyUuids) &&
                Objects.equals(secondFileOnlyUuids, that.secondFileOnlyUuids) &&
                Objects.equals(uuidToFirstFileUniqueTuples, that.uuidToFirstFileUniqueTuples) &&
                Objects.equals(uuidToSecondFileUniqueTuples, that.uuidToSecondFileUniqueTuples);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstFileOnlyUuids, secondFileOnlyUuids, uuidToFirstFileUniqueTuples, uuidToSecondFileUniqueTuples);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UUIDs only from first file:\n");

        firstFileOnlyUuids.forEach(uuid -> stringBuilder.append(uuid).append(";\n"));

        stringBuilder.append("UUIDs only from second file:\n");
        secondFileOnlyUuids.forEach(uuid -> stringBuilder.append(uuid).append(";\n"));

        stringBuilder.append("Unique tuples from first file:\n");
        uuidToFirstFileUniqueTuples.forEach((uuid, tuples) -> stringBuilder
                .append("UUID = ").append(uuid)
                .append(": ").append(tuples).append("\n"));

        stringBuilder.append("Unique tuples from second file:\n");
        uuidToSecondFileUniqueTuples.forEach((uuid, tuples) -> stringBuilder.append("UUID = ").append(uuid)
                .append(": ").append(tuples).append("\n"));

        return stringBuilder.toString();
    }
}
