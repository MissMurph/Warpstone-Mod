package com.lenin.warpstonemod.common.data.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.items.MutateItem;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.weighted_random.WeightModifier;
import com.lenin.warpstonemod.common.weighted_random.WeightModifier.*;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class MutateItemData {

    private final ResourceLocation key;
    private final int corruption;
    private final int instability;
    private final int witherRisk;
    private final Map<ResourceLocation, List<WeightModifier>> modifiers;

    private MutateItemData(Builder builder) {
        key = builder.key;
        corruption = builder.corruption;
        instability = builder.instability;
        witherRisk = builder.witherRisk;

        modifiers = builder.modifiers;
    }

    public JsonObject serialize () {
        JsonObject out = new JsonObject();

        out.addProperty("key", key.toString());
        out.addProperty("corruption", corruption);
        out.addProperty("instability", instability);
        out.addProperty("witherRisk", witherRisk);

        JsonArray array = new JsonArray();

        for (ResourceLocation key : modifiers.keySet()) {
            for (WeightModifier modifier : modifiers.get(key)) {
                JsonObject obj = new JsonObject();

                obj.addProperty("target", key.toString());
                obj.addProperty("value", modifier.getValue());
                obj.addProperty("operation", modifier.getOp().toString());

                array.add(obj);
            }
        }

        out.add("modifiers", array);

        return out;
    }

    public ResourceLocation getKey () {
        return key;
    }

    public static class Builder {
        private final ResourceLocation key;
        private final int corruption;
        private final int instability;
        private final int witherRisk;
        private final Map<ResourceLocation, List<WeightModifier>> modifiers = new HashMap<>();

        public Builder (ResourceLocation _key, int _corruption, int _instability, int _witherRisk) {
            key = _key;
            corruption = _corruption;
            instability = _instability;
            witherRisk = _witherRisk;
        }

        public Builder addModifier (Mutation mutation, float value, Operation operation) {
            return addModifier(mutation.getRegistryName(), value, operation);
        }

        public Builder addModifier (MutationTag tag, float value, Operation operation) {
            return addModifier(tag.getKey(), value, operation);
        }

        public Builder addModifier (ResourceLocation target, float value, Operation operation) {
            List<WeightModifier> list = modifiers.computeIfAbsent(target, key -> new ArrayList<>());

            list.add(new WeightModifier(value, operation));

            return this;
        }

        public MutateItemData build () {
            return new MutateItemData(this);
        }
    }
}