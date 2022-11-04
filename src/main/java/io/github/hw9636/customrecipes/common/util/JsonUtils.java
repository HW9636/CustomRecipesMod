package io.github.hw9636.customrecipes.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {
    public static JsonObject singleton(String name, JsonElement value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(name, value);
        return jsonObject;
    }

    public static JsonObject singleton(String name, boolean bool) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(name, bool);
        return jsonObject;
    }
}
