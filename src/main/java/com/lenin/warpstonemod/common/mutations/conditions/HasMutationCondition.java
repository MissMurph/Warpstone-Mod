package com.lenin.warpstonemod.common.mutations.conditions;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.Mutations;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class HasMutationCondition implements IMutationCondition {

    private final ResourceLocation resource;

    private final ResourceLocation mutationKey;
    private final boolean test;

    private HasMutationCondition (ResourceLocation _key, boolean _test) {
        resource = new ResourceLocation(Warpstone.MOD_ID, "has_mutation");
        mutationKey = _key;
        test = _test;
    }

    @Override
    public ResourceLocation getKey() {
        return resource;
    }

    @Override
    public boolean act(PlayerManager manager) {
        return manager.containsEffect(mutationKey) == test;
    }

    @Override
    public ITextComponent getTooltip() {
        return new TranslationTextComponent("condition." + resource.getPath())
                .appendString(": ")
                .appendSibling(Mutations.getMutation(mutationKey).getMutationName());
    }

    public static IMutationCondition.IBuilder builder (ResourceLocation _key, boolean _test) {
        return () -> new HasMutationCondition(_key, _test);
    }

    public static class Serializer implements IConditionSerializer {
        @Override
        public IMutationCondition deserialize(JsonObject json) {
            return builder(new ResourceLocation(json.get("mutation").getAsString()), json.get("result").getAsBoolean()).build();
        }

        @Override
        public JsonObject serialize(IMutationCondition _condition) {
            JsonObject out = new JsonObject();
            HasMutationCondition condition = (HasMutationCondition)_condition;

            out.addProperty("key", condition.resource.toString());
            out.addProperty("mutation", condition.mutationKey.toString());
            out.addProperty("result", condition.test);

            return out;
        }
    }
}