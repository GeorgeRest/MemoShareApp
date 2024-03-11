package com.george.memoshareapp.utils;

import com.google.gson.Gson;

public class JsonUtils {
    private static Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
