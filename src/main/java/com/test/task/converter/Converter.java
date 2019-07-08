package com.test.task.converter;

public interface Converter<S, T> {

    T convert(S source);
}
