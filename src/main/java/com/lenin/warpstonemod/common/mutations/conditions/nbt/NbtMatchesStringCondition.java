package com.lenin.warpstonemod.common.mutations.conditions.nbt;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.conditions.IConditionSerializer;
import com.lenin.warpstonemod.common.mutations.conditions.IMutationCondition;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

public class NbtMatchesStringCondition extends NbtCondition {

    protected NbtMatchesStringCondition (ResourceLocation _targetMut, String _nbtKey, INBT _nbt) {
        super(new ResourceLocation(Warpstone.MOD_ID, "nbt_matches_string"), _targetMut, _nbtKey, _nbt);
    }

    @Override
    public ResourceLocation getKey() {
        return super.getKey();
    }

    @Override
    public boolean act(PlayerManager manager) {
        return super.act(manager);
    }

    public static IBuilder builder(ResourceLocation _targetMut, String _key, String _value) {
        return () -> new NbtMatchesStringCondition(_targetMut, _key, StringNBT.valueOf(_value));
    }

    public static class Serializer implements IConditionSerializer {
        @Override
        public IMutationCondition deserialize(JsonObject json) {
            return builder(new ResourceLocation(json.get("target_mutation").getAsString()),
                    json.get("target_nbt").getAsString(),
                    json.get("value").getAsString()
            ).build();
        }

        @Override
        public JsonObject serialize(IMutationCondition _condition) {
            JsonObject out = new JsonObject();
            NbtMatchesStringCondition condition = (NbtMatchesStringCondition) _condition;

            out.addProperty("key", condition.registryKey.toString());
            out.addProperty("target_mutation", condition.parent.getRegistryName().toString());
            out.addProperty("target_nbt", condition.nbtKey);
            out.addProperty("value", condition.nbt.getString());

            return out;
        }
    }
}