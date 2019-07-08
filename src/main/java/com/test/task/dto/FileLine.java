package com.test.task.dto;

import java.util.Objects;
import java.util.Set;

public class FileLine {

    private final String uuid;
    private final Set<FileTuple> tuples;

    public FileLine(String uuid, Set<FileTuple> tuples) {
        this.uuid = uuid;
        this.tuples = tuples;
    }

    public String getUuid() {
        return uuid;
    }

    public Set<FileTuple> getTuples() {
        return tuples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileLine fileLine = (FileLine) o;
        return Objects.equals(uuid, fileLine.uuid) &&
                Objects.equals(tuples, fileLine.tuples);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, tuples);
    }
}
