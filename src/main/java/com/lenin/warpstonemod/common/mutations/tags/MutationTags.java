package com.lenin.warpstonemod.common.mutations.tags;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutationTags {

    private static final Map<String, MutationTag> tagMap = new HashMap<>();

    /*public static MutationTag registerTag (String key, int weight) {
        MutationTag tag = new MutationTag(key, weight);
        tagMap.put(key, tag);
        return tag;
    }

    public static MutationTag registerTag (String key, int weight, TextFormatting formatting) {
        MutationTag tag = new MutationTag(key, weight, formatting);
        tagMap.put(key, tag);
        return tag;
    }*/

    public static MutationTag registerTag(String name, MutationTag tag) {
        return tagMap.put(name, tag);
    }

    public static List<MutationTag> getEntries () {
        return new ArrayList<>(tagMap.values());
    }

    public static MutationTag getTag (String key) {
        return tagMap.getOrDefault(key, null);
    }

    public static void loadTagData (JsonObject json) {
        String name = json.get("name").getAsString();
        if (tagMap.containsKey(name)) {
            tagMap.get(name).deserialize(json);
        }
        else {
            registerTag(json.get("name").getAsString(), new MutationTag(json));
        }
    }

    public static void init() {}
}