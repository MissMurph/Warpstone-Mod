package com.lenin.warpstonemod.common.data.mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MutationTreeData {

    private final ResourceLocation key;
    private final ResourceLocation originNode;
    private final List<NodeData> nodes = new ArrayList<>();

    private MutationTreeData (ResourceLocation _key, ResourceLocation _originNode) {
        key = _key;
        originNode = _originNode;
    }

    public JsonObject serialize () {
        JsonObject out = new JsonObject();

        out.addProperty("key", key.toString());
        out.addProperty("origin", originNode.toString());

        JsonArray array = new JsonArray();

        for (NodeData node : nodes) {
            array.add(node.serialize());
        }

        out.add("nodes", array);

        return out;
    }

    public ResourceLocation getKey () {
        return key;
    }

    public static class Builder {
        private final MutationTreeData data;

        public Builder(ResourceLocation _key, NodeData.Builder _originNode) {
            NodeData origin = _originNode.create();
            data = new MutationTreeData(_key, origin.nodeKey);
            data.nodes.add(origin);
        }

        public Builder addNode (NodeData.Builder builder) {
            data.nodes.add(builder.create());
            return this;
        }

        public MutationTreeData create () {
            return data;
        }
    }

    public static class NodeData {
        private final ResourceLocation nodeKey;
        private final int x;
        private final int y;
        private final List<ResourceLocation> nextNodes = new ArrayList<>();

        private NodeData (ResourceLocation _nodeKey, int _x, int _y) {
            nodeKey = _nodeKey;
            x = _x;
            y = _y;
        }

        public JsonObject serialize () {
            JsonObject out = new JsonObject();

            out.addProperty("key", nodeKey.toString());
            out.addProperty("x", x);
            out.addProperty("y", y);

            JsonArray array = new JsonArray();

            for (ResourceLocation res : nextNodes) {
                array.add(res.toString());
            }

            out.add("next", array);

            return out;
        }

        public static class Builder {
            private final NodeData nodeData;

            public Builder(ResourceLocation _nodeKey, int _x, int _y) {
                nodeData = new NodeData(_nodeKey, _x, _y);
            }

            public Builder addNext (ResourceLocation nextKey) {
                nodeData.nextNodes.add(nextKey);
                return this;
            }

            public NodeData create () {
                return nodeData;
            }
        }
    }
}