package com.lenin.warpstonemod.common.data.mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class EvolvingMutationData extends MutationData {

    protected final List<MutationData> childMutations = new ArrayList<>();

    protected EvolvingMutationData() {
        super();
    }

    @Override
    public JsonObject serialize() {
        JsonObject out = super.serialize();

        JsonArray children = new JsonArray();

        for (MutationData mut : childMutations) {
            children.add(mut.serialize());
        }

        out.add("children", children);

        return out;
    }

    public static class Builder extends AbstractBuilder<EvolvingMutationData> {

        public Builder(ResourceLocation _resource) {
            super(_resource, new EvolvingMutationData());
        }

        public Builder addChildEffect (EffectMutationData _data) {
            data.childMutations.add(_data);
            return this;
        }

        public Builder addChildEvolve (EvolvingMutationData _data) {
            data.childMutations.add(_data);
            return this;
        }
    }
}