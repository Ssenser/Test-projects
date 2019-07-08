package com.test.task.converter;

import org.springframework.lang.Nullable;

public interface Converter<S, T> {

    T convert(@Nullable S source);
}
