package com.lenin.warpstonemod.common.mutations.evolving_mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationInstance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class NbtMutationInstance extends MutationInstance {

    protected final Map<String, INBT> data = new HashMap<>();

    public NbtMutationInstance(PlayerManager _parent) {
        super(_parent);
    }

    public INBT readData (String key) {
        return data.get(key);
    }

    public void writeData (String key, INBT _data) {
        data.put(key, _data);
    }
}