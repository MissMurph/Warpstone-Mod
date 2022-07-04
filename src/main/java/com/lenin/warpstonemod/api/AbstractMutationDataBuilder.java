package com.lenin.warpstonemod.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.MutationSupplier;
import com.lenin.warpstonemod.common.mutations.conditions.IMutationCondition;
import com.lenin.warpstonemod.common.mutations.conditions.MutationConditions;
import com.lenin.warpstonemod.common.mutations.conditions.nbt.NbtMatchesStringCondition;
import com.lenin.warpstonemod.common.mutations.conditions.nbt.NbtNumberCondition;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMutationDataBuilder<T extends Mutation> {
    protected T mutation;

    public T get() {
        return mutation;
    }

    protected ResourceLocation parentKey;
    protected String resourcePath;
    protected final List<String> tags = new ArrayList<>();
    protected int weight = 100;
    protected JsonArray conditions = new JsonArray();

    protected <M extends T> AbstractMutationDataBuilder(ResourceLocation key, MutationSupplier<M> supplier) {
        mutation = supplier.get(key);

        resourcePath = "textures/gui/effect_mutations/" + key.getPath() + ".png";
    }

    public AbstractMutationDataBuilder<T> setResourcePath (String _path) {
        resourcePath = _path;
        return this;
    }

    public AbstractMutationDataBuilder<T> addTag (MutationTag tag) {
        return addTag(tag.getKey());
    }

    public AbstractMutationDataBuilder<T> addTag (ResourceLocation tag) {
        tags.add(tag.toString());
        return this;
    }

    public AbstractMutationDataBuilder<T> addCondition(IMutationCondition condition) {
        conditions.add(MutationConditions.getCondition(condition.getKey()).serialize(condition));
        return this;
    }

    public AbstractMutationDataBuilder<T> addNbtStringCondition (String nbtKey, String value) {
        IMutationCondition condition = NbtMatchesStringCondition.builder(mutation.getRegistryName(), nbtKey, value).build();
        conditions.add(MutationConditions.getCondition(condition.getKey()).serialize(condition));
        return this;
    }

    public AbstractMutationDataBuilder<T> addNbtNumberCondition (String nbtKey, NbtNumberCondition.Type type, String value, NbtNumberCondition.Operation... operations) {
        IMutationCondition condition = NbtNumberCondition.builder(mutation.getRegistryName(), nbtKey, type, value, operations).build();
        conditions.add(MutationConditions.getCondition(condition.getKey()).serialize(condition));
        return this;
    }

    public AbstractMutationDataBuilder<T> setWeight (int _weight) {
        weight = _weight;
        return this;
    }

    protected AbstractMutationDataBuilder<T> setParent (ResourceLocation key) {
        parentKey = key;

        JsonArray newConditions = new JsonArray();

        for (JsonElement json : conditions) {
            JsonObject object = json.getAsJsonObject();

            if (object.has("target_mutation") && object.get("target_mutation").getAsString().equals(mutation.getRegistryName().toString())) {
                object.remove("target_mutation");
                object.addProperty("target_mutation", key.toString());
            }

            newConditions.add(object);
        }

        conditions = newConditions;

        return this;
    }

    public JsonObject build () {
        JsonObject out = new JsonObject();

        out.addProperty("key", mutation.getRegistryName().toString());
        out.addProperty("resource_path", resourcePath);
        out.addProperty("weight", weight);

        JsonArray jsonTags = new JsonArray();

        for (String tag : tags) {
            jsonTags.add(tag);
        }

        out.add("tags", jsonTags);
        out.add("conditions", conditions);
        out.add("arguments", mutation.serializeArguments());

        return out;
    }
}