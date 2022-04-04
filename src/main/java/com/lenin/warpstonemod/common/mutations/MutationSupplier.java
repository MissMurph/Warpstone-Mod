package com.lenin.warpstonemod.common.mutations;

import net.minecraft.util.ResourceLocation;

public interface MutationSupplier<T extends Mutation> {
    T get(ResourceLocation key);
}