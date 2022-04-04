package com.lenin.warpstonemod.common.mutations.evolving_mutations;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.Mutations;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import com.lenin.warpstonemod.common.mutations.MutationSupplier;

public abstract class EvolvingMutation extends Mutation {

    protected static final LinkedHashMap<ResourceLocation, Mutation> CHILDREN = new LinkedHashMap<>();
    protected static final Map<ResourceLocation, MutationSupplier<Mutation>> LOCAL_REGISTRY = new HashMap<>();

    public EvolvingMutation(ResourceLocation _key) {
        super(_key);

        fillRegistry();

        for (Map.Entry<ResourceLocation, MutationSupplier<Mutation>> entry : LOCAL_REGISTRY.entrySet()) {
            CHILDREN.put(entry.getKey(), Mutations.register(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public void applyMutation(PlayerManager manager) {
        super.applyMutation(manager);

        //apply effects of first mutation
        CHILDREN.entrySet().iterator().next().getValue().applyMutation(getInstance(manager.getUniqueId()));
    }

    @Override
    public void deactivateMutation(PlayerManager manager) {
        super.deactivateMutation(manager);

        for (Mutation mutation : CHILDREN.values()) {
            mutation.deactivateMutation(manager);
        }
    }

    @Override
    public void clearInstance(PlayerManager manager) {
        super.clearInstance(manager);

        for (Mutation mutation : CHILDREN.values()) {
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
        for (Mutation mutation : CHILDREN.values()) {
            removeInstanceFromChild(instance, mutation);
        }

        addInstanceToChild(instance, moveTo);
    }

    @Override
    public MutationInstance getInstanceType(PlayerManager manager) {
        return new NbtMutationInstance(manager);
    }

    protected abstract void fillRegistry ();

    protected static void registerChild (ResourceLocation _key, MutationSupplier<Mutation> _supplier) {
        LOCAL_REGISTRY.put(_key, _supplier);
    }
}