package io.github.hw9636.customrecipes.common.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    @SafeVarargs
    public static <T> List<T> joinLists(List<T> ... pLists) {
        List<T> toReturn = new ArrayList<>();
        for (List<T> list : pLists) toReturn.addAll(list);
        return toReturn;
    }

    public static <T> List<T> joinLists(List<List<T>> pLists) {
        List<T> toReturn = new ArrayList<>();
        for (List<T> list : pLists) toReturn.addAll(list);
        return toReturn;
    }
}
