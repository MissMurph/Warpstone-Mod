package com.lenin.warpstonemod.common.mutations.conditions.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.conditions.IConditionSerializer;
import com.lenin.warpstonemod.common.mutations.conditions.IMutationCondition;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.NbtMutationInstance;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class NbtNumberCondition extends NbtCondition {

    private final List<Operation> operations;

    protected NbtNumberCondition (ResourceLocation _targetMut, String _nbtKey, INBT _nbt, Operation... _operations) {
        super(new ResourceLocation(WarpstoneMain.MOD_ID, "nbt_number"), _targetMut, _nbtKey, _nbt);

        operations = Arrays.asList(_operations);
    }

    public static IBuilder builder(ResourceLocation _targetMut, String _key, Type _type, String _value, Operation... _operations) {
        return () -> new NbtNumberCondition(_targetMut, _key, Type.constructNbt(_type, _value), _operations);
    }

    //If any one of the operational checks fail then the whole check fails, each check must match for condition to be true
    //If nothing triggers a false return then its safe to return true as all checks have passed
    //This function is quite a mess as we have to account for it being either float or int

    @Override
    public boolean act(PlayerManager manager) {
        if (!(parent.getInstance(manager.getUniqueId()) instanceof NbtMutationInstance)) return false;

        NbtMutationInstance instance = (NbtMutationInstance) parent.getInstance(manager.getUniqueId());
        NumberNBT testData = (NumberNBT) instance.readData(nbtKey);

        for (Operation operation : operations) {
            switch (operation) {
                case EQUAL_TO:
                    if (instance.readData(nbtKey).equals(nbt)) return false;
                case LESS_THAN:
                    if (Type.valueOf(testData.getType().getName()).equals(Type.FLOAT))
                        if (!(testData.getFloat() < testData.getFloat())) return false;
                    else {
                        if (!(testData.getInt() < testData.getInt())) return false; }
                case GREATER_THAN:
                    if (Type.valueOf(testData.getType().getName()).equals(Type.FLOAT))
                        if (!(testData.getFloat() > testData.getFloat())) return false;
                    else {
                        if (!(testData.getInt() > testData.getInt())) return false; }
            }
        }

        return true;
    }

    public static class Serializer implements IConditionSerializer {
        @Override
        public IMutationCondition deserialize(JsonObject json) {
            return builder(new ResourceLocation(json.get("target_mutation").getAsString()),
                    json.get("target_nbt").getAsString(),
                    Type.valueOf(json.get("type").getAsString()),
                    json.get("value").getAsString()
            ).build();
        }

        @Override
        public JsonObject serialize(IMutationCondition _condition) {
            JsonObject out = new JsonObject();
            NbtNumberCondition condition = (NbtNumberCondition) _condition;

            out.addProperty("key", condition.registryKey.toString());
            out.addProperty("target_mutation", condition.parent.getRegistryName().toString());
            out.addProperty("target_nbt", condition.nbtKey);
            out.addProperty("type", condition.nbt.getType().getName());

            String value;

            if (Type.valueOf(condition.nbt.getType().getName()).equals(Type.FLOAT))
                value = condition.nbt.getString().replace("f", "");
            else value = condition.nbt.getString();

            out.addProperty("value", value);

            JsonArray ops = new JsonArray();

            for (Operation op : condition.operations) {
                ops.add(op.toString());
            }

            out.add("operations", ops);

            return out;
        }
    }

    public enum Operation {
        EQUAL_TO,
        GREATER_THAN,
        LESS_THAN
    }

    public enum Type {
        INT,
        FLOAT;

        private static NumberNBT constructNbt (Type type, String value) {
            if (type == Type.FLOAT) return FloatNBT.valueOf(Float.parseFloat(value));
            return IntNBT.valueOf(Integer.parseInt(value));
        }
    }
}