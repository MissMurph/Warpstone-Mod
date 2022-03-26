package com.lenin.warpstonemod.common.data;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.data.mutation_conditions.CorruptionLevelCondition;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MutationConditions {
    private static final Map<ResourceLocation, IMutationCondition> registry = new HashMap<>();

    public static final IMutationCondition CORRUPTION_LEVEL = register(key("corruption_level"), CorruptionLevelCondition::new);

    public static IMutationCondition register (ResourceLocation key, Supplier<IMutationCondition> supplier) {
        IMutationCondition condition = supplier.get();
        registry.put(key, condition);
        return condition;
    }

    public static IMutationCondition getCondition (ResourceLocation key) {
        return registry.get(key);
    }

    private static ResourceLocation key (String key) {
        return new ResourceLocation(WarpstoneMain.MOD_ID, key);
    }
}