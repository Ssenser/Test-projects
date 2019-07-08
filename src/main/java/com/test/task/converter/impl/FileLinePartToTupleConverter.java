package com.test.task.converter.impl;

import com.test.task.converter.Converter;
import com.test.task.dto.FileTuple;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Qualifier("fileLinePartToTupleConverter")
public class FileLinePartToTupleConverter implements Converter<String, Set<FileTuple>> {

    private static final String TUPLES_SPLITERATOR = "\\)\\(";
    private static final String TUPLE_PARTS_SPLITERATOR = " ";

    @Override
    public Set<FileTuple> convert(String source) {
        final Set<FileTuple> tuples = new HashSet<>();
        final String[] tuplesSplitted = source.substring(1, source.length() - 1).split(TUPLES_SPLITERATOR);

        for (String tuplePart : tuplesSplitted) {
            final String[] tuplePartsSplitted = tuplePart.split(TUPLE_PARTS_SPLITERATOR);

            tuples.add(new FileTuple(Long.valueOf(tuplePartsSplitted[0]),
                    Long.valueOf(tuplePartsSplitted[1]), tuplePartsSplitted[2]));
        }

        return tuples;
    }
}
