package com.lenin.warpstonemod.common.mutations.conditions.nbt;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.conditions.IMutationCondition;
import com.lenin.warpstonemod.common.mutations.effect_mutations.Mutations;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.NbtMutationInstance;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;

public abstract class NbtCondition implements IMutationCondition {
    protected final ResourceLocation registryKey;
    protected final Mutation parent;
    protected final String nbtKey;
    protected final INBT nbt;

    protected NbtCondition (ResourceLocation _registryKey, ResourceLocation _targetMut, String _nbtKey, INBT _nbt) {
        registryKey = _registryKey;
        parent = Mutations.getMutation(_targetMut);
        nbtKey = _nbtKey;
        nbt = _nbt;
    }

    @Override
    public ResourceLocation getKey() {
        return registryKey;
    }

    @Override
    public boolean act(PlayerManager manager) {
        return ((NbtMutationInstance) parent.getInstance(manager.getUniqueId())).readData(nbtKey).equals(nbt);
    }
}
