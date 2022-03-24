package com.lenin.warpstonemod.common.mutations.tags;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MutationTag {
    private final ResourceLocation resourceLocation;

    private final List<TextFormatting> formatting;

    private MutationTag.Type type;

    private int weight;

    public MutationTag (JsonObject json) {
        String key = json.get("key").getAsString();
        resourceLocation = new ResourceLocation(key);
        weight = json.get("weight").getAsInt();

        if (json.has("type")) type = Type.valueOf(json.get("type").getAsString());

        List<TextFormatting> _formatting = new ArrayList<>();

        json.getAsJsonArray("formatting").forEach(format -> _formatting.add(TextFormatting.getValueByName(format.getAsString())));

        formatting = _formatting;
    }

    public ResourceLocation getResource () {
        return resourceLocation;
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
        if (json.has("type")) this.type = Type.valueOf(json.get("type").getAsString());
        this.weight = json.get("weight").getAsInt();
    }

    public enum Type {
        RARITY,
        CATEGORY
    }
}