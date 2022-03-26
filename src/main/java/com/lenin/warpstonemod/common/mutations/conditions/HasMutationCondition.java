package com.lenin.warpstonemod.common.mutations.conditions;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import net.minecraft.util.ResourceLocation;

public class HasMutationCondition implements IMutationCondition {

    private final ResourceLocation resource;

    private final ResourceLocation key;
    private final boolean test;

    private HasMutationCondition (ResourceLocation _key, boolean _test) {
        resource = new ResourceLocation(WarpstoneMain.MOD_ID, "has_mutation");
        key = _key;
        test = _test;
    }

    @Override
    public ResourceLocation getKey() {
        return resource;
    }

    @Override
    public boolean act(PlayerManager manager) {
        return manager.containsEffect(key) == test;
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
            out.addProperty("mutation", condition.key.toString());
            out.addProperty("result", condition.test);

            return out;
        }
    }
}