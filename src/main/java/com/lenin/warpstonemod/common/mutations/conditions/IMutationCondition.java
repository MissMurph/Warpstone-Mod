package com.lenin.warpstonemod.common.mutations.conditions;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import net.minecraft.util.ResourceLocation;

import java.util.ResourceBundle;

public interface IMutationCondition {
    ResourceLocation getKey();
    boolean act(PlayerManager manager);

    @FunctionalInterface
    interface IBuilder {
        IMutationCondition build();
    }
}