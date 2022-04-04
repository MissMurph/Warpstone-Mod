package com.lenin.warpstonemod.common.mutations.conditions;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.conditions.nbt.NbtMatchesStringCondition;
import com.lenin.warpstonemod.common.mutations.conditions.nbt.NbtNumberCondition;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MutationConditions {
    private static final Map<ResourceLocation, IConditionSerializer> registry = new HashMap<>();

    public static final IConditionSerializer CORRUPTION_LEVEL = register(key("corruption_level"), new CorruptionLevelCondition.Serializer());
    public static final IConditionSerializer HAS_MUTATION = register(key("has_mutation"), new HasMutationCondition.Serializer());
    public static final IConditionSerializer NBT_MATCHES_STRING = register(key("nbt_matches_string"), new NbtMatchesStringCondition.Serializer());
    public static final IConditionSerializer NBT_NUMBER = register(key("nbt_number"), new NbtNumberCondition.Serializer());

    public static IConditionSerializer register (ResourceLocation key, IConditionSerializer serializer) {
        registry.put(key, serializer);
        return serializer;
    }

    public static IConditionSerializer getCondition (ResourceLocation key) {
        return registry.get(key);
    }

    public static void init () {}

    private static ResourceLocation key (String key) {
        return new ResourceLocation(Warpstone.MOD_ID, key);
    }
}