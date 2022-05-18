package com.lenin.warpstonemod.common.mutations.evolving_mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.Mutations;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

public class MutationTree {

    //private final ResourceLocation parentKey;
    private final Map<ResourceLocation, Node> nodes = new HashMap<>();
    private final Map<UUID, Node> instanceNodes = new HashMap<>();
    private final ResourceLocation originNodeKey;

    public MutationTree (JsonObject json) {
        //parentKey = new ResourceLocation(json.get("key").getAsString());
        originNodeKey = new ResourceLocation(json.get("origin").getAsString());

        JsonArray nodeArray = json.getAsJsonArray("nodes");

        for (JsonElement element : nodeArray) {
            Node node = new Node(element.getAsJsonObject());
            nodes.put(node.getRegistryKey(), node);
        }
    }

    public void advanceInstance (UUID uuid, ResourceLocation target) {
        Node current = instanceNodes.get(uuid);

        if (!current.getAll().contains(nodes.get(target))) throw new IllegalArgumentException("Can't move Instance, target Mutation isn't next in tree");

        instanceNodes.put(uuid, nodes.get(target));
    }

    public Node putInstance (UUID uuid) {
        return instanceNodes.put(uuid, getOrigin());
    }

    public void clearInstance (UUID uuid) {
        instanceNodes.remove(uuid);
    }

    public Mutation getMutation (UUID uuid) {
        return getCurrentNode(uuid).getParent();
    }

    public Node getOrigin () {
        return nodes.get(originNodeKey);
    }

    public Node getNode (ResourceLocation key) {
        return nodes.get(key);
    }

    public Node getCurrentNode (UUID uuid) {
        return instanceNodes.get(uuid);
    }

    public List<Node> getAll () {
        return new ArrayList<>(nodes.values());
    }

    public CompoundNBT saveInstance (UUID uuid) {
        CompoundNBT out = new CompoundNBT();
        out.putString("uuid", uuid.toString());
        out.putString("current_mutation", getCurrentNode(uuid).getRegistryKey().toString());
        return out;
    }

    public Node loadInstance (CompoundNBT nbt) {
        return instanceNodes.put(UUID.fromString(nbt.getString("uuid")), getNode(new ResourceLocation(nbt.getString("current_mutation"))));
    }

    public class Node {
        private final int x;
        private final int y;
        private final ResourceLocation parentKey;
        private final List<ResourceLocation> next = new ArrayList<>();
        private final List<ResourceLocation> optional = new ArrayList<>();

        public Node (JsonObject json) {
            parentKey = new ResourceLocation(json.get("key").getAsString());
            x = json.get("x").getAsInt();
            y = json.get("y").getAsInt();

            JsonArray nextNodes = json.getAsJsonArray("next");

            for (JsonElement element : nextNodes) {
                next.add(new ResourceLocation(element.getAsString()));
            }

            JsonArray optionalNodes = json.getAsJsonArray("optional");

            for (JsonElement element : optionalNodes) {
                optional.add(new ResourceLocation(element.getAsString()));
            }
        }

        public List<Node> getNext () {
            return nodes.entrySet().stream()
                    .filter(entry -> next.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }

        public List<Node> getOptional () {
            return nodes.entrySet().stream()
                    .filter(entry -> optional.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }

        public List<Node> getAll () {
            List<Node> combined = getNext();
            combined.addAll(getOptional());
            return combined;
        }

        public int getX () {
            return x;
        }

        public int getY () {
            return y;
        }

        public ResourceLocation getRegistryKey () {
            return parentKey;
        }

        public Mutation getParent () {
            return Mutations.getMutation(parentKey);
        }
    }
}