package com.lenin.warpstonemod.api;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONBuffer {

    private static final Map<String, List<JsonObject>> outputMap = new HashMap<>();

    public static void primeData (String name, JsonObject json) {
        List<JsonObject> list = outputMap.computeIfAbsent(name, key -> new ArrayList<>());
        list.add(json);
    }

    public static void init () {}
}