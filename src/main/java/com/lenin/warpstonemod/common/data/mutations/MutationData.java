package com.lenin.warpstonemod.common.data.mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.mutations.conditions.IMutationCondition;
import com.lenin.warpstonemod.common.mutations.conditions.MutationConditions;
import com.lenin.warpstonemod.common.mutations.conditions.nbt.NbtMatchesStringCondition;
import com.lenin.warpstonemod.common.mutations.conditions.nbt.NbtNumberCondition;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class MutationData {
    protected ResourceLocation resource;
    protected ResourceLocation parentKey;
    protected String resourcePath;
    protected final List<String> tags = new ArrayList<>();
    protected int weight = 100;

    //protected final List<IMutationCondition> conditions = new ArrayList<>();
    protected JsonArray conditions = new JsonArray();

    protected MutationData () {}

    public JsonObject serialize () {
        JsonObject out = new JsonObject();

        out.addProperty("key", resource.toString());
        out.addProperty("resource_path", resourcePath);
        out.addProperty("weight", weight);

        JsonArray jsonTags = new JsonArray();

        for (String tag : tags) {
            jsonTags.add(tag);
        }

        out.add("tags", jsonTags);

        out.add("conditions", conditions);

        if (Registration.MUTATIONS.containsKey(resource)) out.add("arguments", Registration.MUTATIONS.getValue(resource).serializeArguments());
        else out.add("arguments", new JsonObject());

        return out;
    }

    public String getPath () {
        return resource.getPath();
    }

    public abstract static class AbstractBuilder<T extends MutationData> {
        protected final T data;

        public AbstractBuilder(ResourceLocation _resource, T _data) {
            data = _data;

            data.resource = _resource;
            data.resourcePath = "textures/gui/effect_mutations/" + _resource.getPath() + ".png";
        }

        public AbstractBuilder<T> addResourcePath (String _path) {
            data.resourcePath = _path;
            return this;
        }

        public AbstractBuilder<T> addTag (ResourceLocation tag) {
            data.tags.add(tag.toString());
            return this;
        }

        public AbstractBuilder<T> addCondition(IMutationCondition condition) {
            data.conditions.add(MutationConditions.getCondition(condition.getKey()).serialize(condition));
            return this;
        }

        public AbstractBuilder<T> addNbtStringCondition (String nbtKey, String value) {
            IMutationCondition condition = NbtMatchesStringCondition.builder(data.resource, nbtKey, value).build();
            data.conditions.add(MutationConditions.getCondition(condition.getKey()).serialize(condition));
            return this;
        }

        public AbstractBuilder<T> addNbtNumberCondition (String nbtKey, NbtNumberCondition.Type type, String value, NbtNumberCondition.Operation... operations) {
            IMutationCondition condition = NbtNumberCondition.builder(data.resource, nbtKey, type, value, operations).build();
            data.conditions.add(MutationConditions.getCondition(condition.getKey()).serialize(condition));
            return this;
        }

        public AbstractBuilder<T> setWeight (int weight) {
            data.weight = weight;
            return this;
        }

        protected AbstractBuilder<T> setParent (ResourceLocation key) {
            data.parentKey = key;

            JsonArray newConditions = new JsonArray();

            for (JsonElement json : data.conditions) {
                JsonObject object = json.getAsJsonObject();

                if (object.has("target_mutation") && object.get("target_mutation").getAsString().equals(data.resource.toString())) {
                    object.remove("target_mutation");
                    object.addProperty("target_mutation", key.toString());
                }

                newConditions.add(object);
            }

            data.conditions = newConditions;

            return this;
        }

        public T create () {
            return data;
        }
    }
}