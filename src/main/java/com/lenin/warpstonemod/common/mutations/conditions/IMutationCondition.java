package com.lenin.warpstonemod.common.mutations.conditions;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public interface IMutationCondition {
    ResourceLocation getKey();
    boolean act(PlayerManager manager);
    ITextComponent getTooltip();

    @FunctionalInterface
    interface IBuilder {
        IMutationCondition build();
    }
}