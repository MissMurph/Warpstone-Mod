package com.lenin.warpstonemod.common.mutations.conditions.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.conditions.IConditionSerializer;
import com.lenin.warpstonemod.common.mutations.conditions.IMutationCondition;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutationInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NbtNumberCondition extends NbtCondition {

    private final List<Operation> operations;

    protected NbtNumberCondition (ResourceLocation _targetMut, String _nbtKey, INBT _nbt, Operation... _operations) {
        super(Warpstone.key("nbt_number"), _targetMut, _nbtKey, _nbt);

        operations = Arrays.asList(_operations);
    }

    public static IBuilder builder(ResourceLocation _targetMut, String _key, Type _type, String _value, Operation... _operations) {
        return () -> new NbtNumberCondition(_targetMut, _key, Type.constructNbt(_type, _value), _operations);
    }

    @Override
    public boolean act(PlayerManager manager) {
        if (!(parent.getInstance(manager.getUniqueId()) instanceof EvolvingMutationInstance)) return false;

        EvolvingMutationInstance instance = (EvolvingMutationInstance) parent.getInstance(manager.getUniqueId());
        NumberNBT requiredValue = (NumberNBT) nbt;
        NumberNBT testValue = (NumberNBT) instance.readData(nbtKey);

        for (Operation operation : operations) {
            switch (operation) {
                case EQUAL_TO:
                    if (testValue.equals(requiredValue)) return true;
                case LESS_THAN:
                    if (Type.valueOf(testValue.getType().getName()).equals(Type.FLOAT))
                        if (testValue.getFloat() < requiredValue.getFloat()) return true;
                    else {
                        if (testValue.getInt() < requiredValue.getInt()) return true; }
                case GREATER_THAN:
                    if (Type.valueOf(testValue.getType().getName()).equals(Type.FLOAT))
                        if (testValue.getFloat() > requiredValue.getFloat()) return true;
                    else {
                        if (testValue.getInt() > requiredValue.getInt()) return true; }
            }
        }

        return false;
    }

    public static class Serializer implements IConditionSerializer {
        @Override
        public IMutationCondition deserialize(JsonObject json) {
            List<Operation> operations = new ArrayList<>();

            json.getAsJsonArray("operations").forEach(op -> operations.add(Operation.valueOf(op.getAsString())));

            return builder(new ResourceLocation(json.get("target_mutation").getAsString()),
                    json.get("target_nbt").getAsString(),
                    Type.valueOf(json.get("type").getAsString()),
                    json.get("value").getAsString(),
                    operations.toArray(new Operation[0])
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