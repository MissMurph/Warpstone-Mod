package com.lenin.warpstonemod.common.mutations.evolving_mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
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
    }

    @Override
    public CompoundNBT saveData (PlayerManager manager) {
        CompoundNBT out = new CompoundNBT();
        EvolvingMutationInstance instance = (EvolvingMutationInstance) getInstance(manager);

        out.put("tree_data", TREE.saveInstance(manager.getUniqueId()));

        ListNBT dataList = new ListNBT();

        for (Map.Entry<String, INBT> entry : instance.data.entrySet()) {
            CompoundNBT dataNbt = new CompoundNBT();

            dataNbt.putString("uuid", instance.getParent().getUniqueId().toString());
            dataNbt.putString("name", entry.getKey());
            dataNbt.put("value", entry.getValue());

            dataList.add(dataNbt);
        }

        out.put("data", dataList);

        return out;
    }

    @Override
    public void loadData (PlayerManager manager, CompoundNBT nbt) {
        super.loadData(manager, nbt);

        MutationTree.Node node = TREE.loadInstance(nbt.getCompound("tree_data"));

        ListNBT dataList = (ListNBT) nbt.get("data");

        if (dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                CompoundNBT data = dataList.getCompound(i);

                EvolvingMutationInstance instance = (EvolvingMutationInstance) getInstance(UUID.fromString(data.getString("uuid")));

                String key = data.getString("name");
                INBT value = data.get("value");

                instance.writeData(key, value);
            }
        }

        node.getParent().applyMutation(manager);
    }

    @Override
    public void deserialize(JsonObject json) {
        super.deserialize(json);

        JsonArray _children = json.getAsJsonArray("children");

        for (JsonElement child : _children) {
            Mutations.loadMutationData(child.getAsJsonObject());
        }
    }

    protected void moveInstanceToChild (MutationInstance instance, Mutation moveTo) {
        MutationTree.Node currentNode = TREE.getCurrentNode(instance.getParent().getUniqueId());
        MutationTree.Node chosenNode = TREE.getNode(moveTo.getRegistryName());

        if (!currentNode.getNext().contains(chosenNode))
            throw new IllegalArgumentException("Can't move Instance, target Mutation isn't next in tree");

        Mutation fromMutation = currentNode.getParent();
        TREE.advanceInstance(instance.getParent().getUniqueId(), currentNode.getNext().indexOf(chosenNode));

        fromMutation.clearMutation(instance.getParent());
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
        return TREE.getOrigin().getParent().getTexture();
    }

    public ResourceLocation getCurrentTexture() {
        return TREE.getCurrentNode(Minecraft.getInstance().player.getUniqueID()).getParent().getTexture();
    }

    public List<MutationTree.Node> getChildNodes () {
        return TREE.getAll();
    }
}