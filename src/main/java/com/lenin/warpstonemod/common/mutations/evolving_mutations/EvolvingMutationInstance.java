package com.lenin.warpstonemod.common.mutations.evolving_mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationInstance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EvolvingMutationInstance extends MutationInstance {

    protected final Map<String, INBT> data = new HashMap<>();

    protected Mutation currentMutation;

    public EvolvingMutationInstance(PlayerManager _parent) {
        super(_parent);
    }

    public INBT readData (String key) {
        return data.get(key);
    }

    public void writeData (String key, INBT _data) {
        data.put(key, _data);
    }

    public void writeIfAbsent (String key, INBT _data) {
        if (!data.containsKey(key)) data.put(key, _data);
    }

    public boolean moveInstance (Mutation from, Mutation to) {
        if (!currentMutation.equals(from) && !currentMutation.equals(to)) return false;

        currentMutation = to;

        return true;
    }
}