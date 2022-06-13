package com.lenin.warpstonemod.common.data.tags;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class MutationTagData {

    private ResourceLocation resource;
    private String type;
    private int weight;
    private final List<String> formatting = new ArrayList<>();

    private MutationTagData () {}

    public String getPath () {
        return resource.getPath();
    }

    public JsonObject serialize () {
        JsonObject json = new JsonObject();
        json.addProperty("key", resource.toString());
        json.addProperty("type", type);
        json.addProperty("weight", weight);

        JsonArray array = new JsonArray();

        for (String format : formatting) {
            array.add(format);
        }

        json.add("formatting", array);

        return json;
    }

    public static class Builder {
        private final MutationTagData data;

        public Builder (ResourceLocation _resource, int _weight) {
            data = new MutationTagData();
            data.resource = _resource;
            data.weight = _weight;
        }

        public Builder addFormatting (TextFormatting formatting) {
            return addFormatting(formatting.getFriendlyName());
        }

        public Builder addFormatting (String formatting) {
            data.formatting.add(formatting);
            return this;
        }

        public Builder addType (MutationTag.Type type) {
            return addType(type.toString());
        }

        public Builder addType (String type) {
            data.type = type;
            return this;
        }

        public MutationTagData create () {
            return data;
        }
    }
}