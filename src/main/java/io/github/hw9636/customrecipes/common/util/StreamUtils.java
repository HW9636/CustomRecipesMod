package io.github.hw9636.customrecipes.common.util;

import java.util.stream.Stream;

public class StreamUtils {
    public static <T> Stream<T> fromIterable(Iterable<T> iterable) {
        Stream.Builder<T> builder = Stream.builder();
        for (T t : iterable) builder.add(t);
        return builder.build();
    }
}
