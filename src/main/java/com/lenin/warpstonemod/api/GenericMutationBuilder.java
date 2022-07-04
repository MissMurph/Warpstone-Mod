package com.lenin.warpstonemod.api;

import com.lenin.warpstonemod.common.data.mutations.MutationData;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.MutationSupplier;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.GenericMutation;
import net.minecraft.util.ResourceLocation;

public class GenericMutationBuilder<T extends Mutation> extends AbstractMutationDataBuilder<T> {

    public GenericMutationBuilder(ResourceLocation key, MutationSupplier<T> supplier) {
        super(key, supplier);
    }
}