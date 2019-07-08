package com.test.task.converter.impl;

import com.test.task.converter.Converter;
import com.test.task.dto.FileLine;
import com.test.task.dto.FileTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FileLineToDtoConverter implements Converter<String, FileLine> {

    private static final String LINE_SPLITERATOR = "\t";

    @Autowired
    private Converter<String, Set<FileTuple>> fileLinePartToTupleConverter;

    @Override
    public FileLine convert(@Nullable String source) {
        final String[] lineSplitted = source.split(LINE_SPLITERATOR);
        final String uuid = lineSplitted[0];
        final String tuples = lineSplitted[1];

        return new FileLine(uuid, fileLinePartToTupleConverter.convert(tuples));
    }
}
