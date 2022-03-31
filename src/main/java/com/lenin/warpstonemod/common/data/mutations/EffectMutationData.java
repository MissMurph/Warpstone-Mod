package com.lenin.warpstonemod.common.data.mutations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EffectMutationData extends MutationData {

    protected final List<AttrModifierData> modifiers = new ArrayList<>();

    protected ResourceLocation parentKey;

    protected EffectMutationData() {
        super();
    }

    @Override
    public JsonObject serialize() {
        JsonObject out = super.serialize();

        JsonArray jsonMods = new JsonArray();

        for (AttrModifierData modifier : modifiers) {
            jsonMods.add(modifier.serialize(this));
        }

        out.add("modifiers", jsonMods);

        return out;
    }

    public static class Builder extends AbstractBuilder<EffectMutationData> {

        public Builder(ResourceLocation _resource) {
            super(_resource, new EffectMutationData());
        }

        public Builder addModifier (ResourceLocation target, double value, String operation) {
            return addModifier(target, data.resource.getPath(), value, operation);
        }

        public Builder addModifier (ResourceLocation target, String name, double value, String operation) {
            data.modifiers.add(new AttrModifierData(target.toString(), name, value, operation));
            return this;
        }
    }

    private static class AttrModifierData {
        private final String target;
        private final String name;
        private final double value;
        private final String operation;

        private AttrModifierData (String _target, String _name, double _value, String _operation) {
            target = _target;
            name = _name;
            value = _value;
            operation = _operation;
        }

        private JsonObject serialize (MutationData data) {
            JsonObject out = new JsonObject();

            out.addProperty("target", target);
            if (!Objects.equals(data.resource.getPath(), this.name)) out.addProperty("name", name);
            out.addProperty("value", value);
            out.addProperty("operation", operation);

            return out;
        }
    }
}