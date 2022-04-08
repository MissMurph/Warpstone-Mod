package com.lenin.warpstonemod.common.mutations.evolving_mutations;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.Mutations;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

import com.lenin.warpstonemod.common.mutations.MutationSupplier;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

public abstract class EvolvingMutation extends Mutation {

    protected static MutationTree TREE;

    public EvolvingMutation (ResourceLocation _key) {
        super(_key);
    }

    @Override
    public void applyMutation (PlayerManager manager) {
        super.applyMutation(manager);

        TREE.putInstance(manager.getUniqueId());
        TREE.getMutation(manager.getUniqueId()).applyMutation(getInstance(manager));
    }

    @Override
    public void deactivateMutation (PlayerManager manager) {
        super.deactivateMutation(manager);

        TREE.getMutation(manager.getUniqueId()).deactivateMutation(manager);
    }

    @Override
    public void clearInstance (PlayerManager manager) {
        super.clearInstance(manager);

        TREE.getMutation(manager.getUniqueId()).clearInstance(manager);
    }

    @Override
    public CompoundNBT saveData () {
        return super.saveData();
    }

    @Override
    public void loadData (CompoundNBT nbt) {
        super.loadData(nbt);
    }

    protected void moveInstanceToChild (MutationInstance instance, Mutation moveTo) {
        MutationTree.Node currentNode = TREE.getCurrentNode(instance.getParent().getUniqueId());
        MutationTree.Node chosenNode = TREE.getNode(moveTo.getRegistryName());

        if (!currentNode.getNext().contains(chosenNode))
            throw new IllegalArgumentException("Can't move Instance, target Mutation isn't next in tree");

        Mutation fromMutation = currentNode.getParent();
        TREE.advanceInstance(instance.getParent().getUniqueId(), currentNode.getNext().indexOf(chosenNode));

        fromMutation.clearInstance(instance.getParent());
        moveTo.applyMutation(instance);
    }

    protected List<Mutation> checkNextConditions (PlayerManager manager) {
        List<Mutation> next = TREE.getCurrentNode(manager.getUniqueId()).getNext().stream()
                .map(MutationTree.Node::getParent)
                .collect(Collectors.toList());

        if (next.size() == 1 && next.get(0).isLegalMutation(manager)) {
            moveInstanceToChild(getInstance(manager), next.get(0));

            return next;
        }

        return next.stream()
                .filter(mutation -> mutation.isLegalMutation(manager))
                .collect(Collectors.toList());
    }

    @Override
    public MutationInstance getInstanceType (PlayerManager manager) {
        return new EvolvingMutationInstance(manager);
    }

    protected static Mutation registerChild (ResourceLocation _key, MutationSupplier<Mutation> _supplier) {
        return Mutations.register(_key, _supplier);
    }

    public void loadTreeData (JsonObject json) {
        TREE = new MutationTree(json);
    }

        //This is only ever going to be called from client side so we're safe to assume the player using
    @Override
    public List<ITextComponent> getToolTips() {
        return TREE.getCurrentNode(Minecraft.getInstance().player.getUniqueID()).getParent().getToolTips();
    }

    @Override
    public ResourceLocation getTexture() {
        return TREE.getCurrentNode(Minecraft.getInstance().player.getUniqueID()).getParent().getTexture();
    }

    public List<MutationTree.Node> getChildNodes () {
        return TREE.getAll();
    }
}