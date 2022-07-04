package com.lenin.warpstonemod.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.MutationSupplier;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EffectMutationBuilder extends AbstractMutationDataBuilder<EffectMutation> {
    protected final List<AttrModifierData> modifiers = new ArrayList<>();

    public EffectMutationBuilder(ResourceLocation _resource) {
        super(_resource, EffectMutation::new);
    }

    //used for custom implementation of this concrete class, substituting the usual construction with custom implementation
    public <T extends EffectMutation> EffectMutationBuilder(ResourceLocation _resource, MutationSupplier<T> supplier) {
        super(_resource, supplier);
    }

    public EffectMutationBuilder addModifier (Attribute attribute, double value, AttributeModifier.Operation operation) {
        return addModifier(attribute.getRegistryName(), mutation.getRegistryName().getPath(), value, operation.toString());
    }

    public EffectMutationBuilder addModifier (ResourceLocation target, double value, String operation) {
        return addModifier(target, mutation.getRegistryName().getPath(), value, operation);
    }

    public EffectMutationBuilder addModifier (ResourceLocation target, String name, double value, String operation) {
        modifiers.add(new AttrModifierData(target.toString(), name, value, operation));
        return this;
    }

    @Override
    public JsonObject build() {
        JsonObject out = super.build();

        JsonArray jsonMods = new JsonArray();

        for (AttrModifierData modifier : modifiers) {
            jsonMods.add(modifier.serialize(mutation));
        }

        out.add("modifiers", jsonMods);

        return out;
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

        private JsonObject serialize (Mutation mutation) {
            JsonObject out = new JsonObject();

            out.addProperty("target", target);
            if (!Objects.equals(mutation.getRegistryName().getPath(), this.name)) out.addProperty("name", name);
            out.addProperty("value", value);
            out.addProperty("operation", operation);

            return out;
        }
    }
}