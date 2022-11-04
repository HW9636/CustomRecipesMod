package io.github.hw9636.customrecipes.common.util;

import java.util.function.Function;

public class FunctionUtils {
    public static <T, R> Function<T, R> nonNullFunction(Function<T, R> pFunction, R pIfNull) {
        return (t) -> {
            R r = pFunction.apply(t);
            if (r == null) return pIfNull;
            return r;
        };
    }
}
