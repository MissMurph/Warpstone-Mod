package com.lenin.warpstonemod.common.mutations.evolving_mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.api.AbstractMutationDataBuilder;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.MutationInstance;
import com.lenin.warpstonemod.common.mutations.Mutations;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

import com.lenin.warpstonemod.common.mutations.MutationSupplier;
import net.minecraft.util.text.ITextComponent;

public abstract class EvolvingMutation extends Mutation {

    protected static MutationTree TREE;

    public EvolvingMutation (ResourceLocation _key) {
        super(_key);
    }

    @Override
    public void applyMutation (PlayerManager manager) {
        super.applyMutation(manager);

        TREE.putInstance(manager.getUniqueId()).getParent().applyMutation(getInstance(manager));
    }

    @Override
    public void clearMutation(PlayerManager manager) {
        super.clearMutation(manager);

        TREE.getMutation(manager.getUniqueId()).clearMutation(manager);
        TREE.clearInstance(manager.getUniqueId());
    }

    @Override
    public CompoundNBT saveData (PlayerManager manager) {
        CompoundNBT out = new CompoundNBT();
        EvolvingMutationInstance instance = (EvolvingMutationInstance) getInstance(manager);

        out.put("tree_data", TREE.saveInstance(manager.getUniqueId()));

        ListNBT dataList = new ListNBT();

        for (Map.Entry<String, INBT> entry : instance.data.entrySet()) {
            CompoundNBT dataNbt = new CompoundNBT();

            dataNbt.putString("name", entry.getKey());
            dataNbt.put("value", entry.getValue());

            dataList.add(dataNbt);
        }

        out.put("data", dataList);

        return out;
    }

    @Override
    public void loadData (PlayerManager manager, CompoundNBT nbt) {
        MutationTree.Node node = TREE.loadInstance(nbt.getCompound("tree_data"));

        ListNBT dataList = (ListNBT) nbt.get("data");

        EvolvingMutationInstance instance =  (EvolvingMutationInstance) getInstance(manager);

        if (dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                CompoundNBT data = dataList.getCompound(i);

                String key = data.getString("name");
                INBT value = data.get("value");

                instance.writeData(key, value);
            }
        }

        node.getParent().applyMutation(instance);
    }

    @Override
    public void deserialize(JsonObject json) {
        super.deserialize(json);

        JsonArray _children = json.getAsJsonArray("children");

        for (JsonElement child : _children) {
            Mutations.loadMutationData(child.getAsJsonObject());
        }
    }

    private void moveInstanceToChild (MutationInstance instance, Mutation moveTo) {
        MutationTree.Node currentNode = TREE.getCurrentNode(instance.getParent().getUniqueId());
        MutationTree.Node chosenNode = TREE.getNode(moveTo.getRegistryName());

        Mutation fromMutation = currentNode.getParent();
        TREE.advanceInstance(instance.getParent().getUniqueId(), chosenNode.getRegistryKey());

        fromMutation.clearMutation(instance.getParent());
        moveTo.applyMutation(instance);
    }

    public void chooseOptional (PlayerManager manager, Mutation moveTo) {
        MutationTree.Node chosenNode = TREE.getNode(moveTo.getRegistryName());

        if (!chosenNode.getParent().isLegalMutation(manager))
            throw new IllegalArgumentException("Can't move Instance, target Mutation's conditions haven't been met");

        moveInstanceToChild(getInstance(manager), moveTo);
    }

    protected void checkNextConditions (PlayerManager manager) {
        List<Mutation> next = TREE.getCurrentNode(manager.getUniqueId()).getNext().stream()
                .map(MutationTree.Node::getParent)
                .collect(Collectors.toList());

        for (Mutation mutation : next) {
            if (mutation.isLegalMutation(manager)) {
                moveInstanceToChild(getInstance(manager), mutation);
                return;
            }
        }
    }

    public MutationTree.Node getCurrentNode(UUID uuid) {
        return TREE.getCurrentNode(uuid);
    }

    @Override
    public MutationInstance getInstanceType (PlayerManager manager) {
        return new EvolvingMutationInstance(manager);
    }

    protected static <M extends Mutation, B extends AbstractMutationDataBuilder<M>> M registerChild (B _builder) {
        return Mutations.register(_builder);
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
        return TREE.getOrigin().getParent().getTexture();
    }

    public ResourceLocation getCurrentTexture() {
        return TREE.getCurrentNode(Minecraft.getInstance().player.getUniqueID()).getParent().getTexture();
    }

    public List<MutationTree.Node> getChildNodes () {
        return TREE.getAll();
    }
}