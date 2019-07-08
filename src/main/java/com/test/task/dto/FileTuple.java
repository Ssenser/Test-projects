package com.test.task.dto;

import java.util.Objects;

public class FileTuple {
    private final Long eventId;
    private final Long dateCreatedInMs;
    private final String host;

    public FileTuple(Long eventId, Long dateCreatedInMs, String host) {
        this.eventId = eventId;
        this.dateCreatedInMs = dateCreatedInMs;
        this.host = host;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getDateCreatedInMs() {
        return dateCreatedInMs;
    }

    public String getHost() {
        return host;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileTuple fileTuple = (FileTuple) o;
        return Objects.equals(eventId, fileTuple.eventId) &&
                Objects.equals(dateCreatedInMs, fileTuple.dateCreatedInMs) &&
                Objects.equals(host, fileTuple.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, dateCreatedInMs, host);
    }

    @Override
    public String toString() {
        return "(EventId = " + eventId + ", date created = " + dateCreatedInMs + ", host = '" + host + "\'); ";
    }
}
