package com.lenin.warpstonemod.common.mutations.tags;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MutationTag {
    private final ResourceLocation key;

    private Type type;

    private List<TextFormatting> formatting;

    private TextFormatting textFormat;

    private int weight;

    public MutationTag (ResourceLocation _key, TextFormatting... _formatting) {
        key = _key;

        formatting = new ArrayList<>();

        Collections.addAll(formatting, _formatting);
    }

    public ResourceLocation getKey() {
        return key;
    }

    public List<TextFormatting> getFormatting () {
        return formatting;
    }

    public MutationTag.Type getType () {
        return type;
    }

    public int getWeight () {
        return weight;
    }

    public MutationTag addType (MutationTag.Type _type) {
        type = _type;

        return this;
    }

    /*public JsonObject serialize () {
        JsonObject json = new JsonObject();
        json.addProperty("name", key);
        if (type != null) json.addProperty("type", type.toString());
        json.addProperty("weight", weight);

        JsonArray array = new JsonArray();

        for (TextFormatting format : formatting) {
            array.add(format.getFriendlyName());
        }

        json.add("formatting", array);

        return json;
    }*/

    public void deserialize (JsonObject json) {
        this.type = Type.valueOf(json.get("type").getAsString());
        this.weight = json.get("weight").getAsInt();

        List<TextFormatting> _formatting = new ArrayList<>();

        json.getAsJsonArray("formatting").forEach(format -> _formatting.add(TextFormatting.getValueByName(format.getAsString())));

        formatting = _formatting;
    }

    public enum Type {
        RARITY,
        CATEGORY,
        ATTRIBUTE
    }
}