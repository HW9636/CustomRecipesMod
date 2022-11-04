package io.github.hw9636.customrecipes.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ArrayUtils {
    public static <T, R> List<R> mapAll(T[] pArray, Function<T, R> pMapFunction) {
        List<R> toReturn = new ArrayList<>(pArray.length);
        for (T t : pArray) toReturn.add(pMapFunction.apply(t));
        return toReturn;
    }
}
