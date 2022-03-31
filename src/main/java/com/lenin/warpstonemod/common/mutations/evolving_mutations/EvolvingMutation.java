package com.lenin.warpstonemod.common.mutations.evolving_mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationInstance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public abstract class EvolvingMutation extends Mutation {

    protected final LinkedHashMap<ResourceLocation, Mutation> childMutations = new LinkedHashMap<>();

    public EvolvingMutation(String _name) {
        super(_name);
    }

    @Override
    public void applyMutation(PlayerManager manager) {
        super.applyMutation(manager);

        //apply effects of first mutation
        childMutations.entrySet().iterator().next().getValue().applyMutation(getInstance(manager.getUniqueId()));
    }

    @Override
    public void deactivateMutation(PlayerManager manager) {
        super.deactivateMutation(manager);

        for (Mutation mutation : childMutations.values()) {
            mutation.deactivateMutation(manager);
        }
    }

    @Override
    public void clearInstance(PlayerManager manager) {
        super.clearInstance(manager);

        for (Mutation mutation : childMutations.values()) {
            mutation.clearInstance(manager);
        }
    }

    @Override
    public CompoundNBT saveData() {
        return super.saveData();
    }

    @Override
    public void loadData(CompoundNBT nbt) {
        super.loadData(nbt);
    }

    protected void addInstanceToChild (MutationInstance instance, Mutation target) {
        target.applyMutation(instance);
    }

    protected void removeInstanceFromChild (MutationInstance instance, Mutation target) {
        target.clearInstance(instance.getParent());
    }

    protected void moveInstanceToChild (MutationInstance instance, Mutation moveTo) {
        for (Mutation mutation : childMutations.values()) {
            removeInstanceFromChild(instance, mutation);
        }

        addInstanceToChild(instance, moveTo);
    }

    @Override
    public MutationInstance getInstanceType(PlayerManager manager) {
        return new NbtMutationInstance(manager);
    }
}