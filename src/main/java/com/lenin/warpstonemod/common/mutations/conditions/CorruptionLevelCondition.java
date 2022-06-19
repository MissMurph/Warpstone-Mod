package com.lenin.warpstonemod.common.mutations.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.PlayerManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorruptionLevelCondition implements IMutationCondition {

    private final ResourceLocation resource;

    private final int value;
    private final List<Operation> operations;

    private CorruptionLevelCondition (int _value, Operation... _operations) {
        resource = new ResourceLocation(Warpstone.MOD_ID, "corruption_level");
        value = _value;
        operations = Arrays.asList(_operations);
    }

    @Override
    public boolean act(PlayerManager manager) {
        List<Boolean> results = new ArrayList<>();

        for (Operation operation : operations) {
            switch (operation) {
                case EQUAL_TO:
                    results.add(manager.getCorruptionLevel() == value);
                    break;
                case LESS_THAN:
                    results.add(manager.getCorruptionLevel() < value);
                    break;
                case GREATER_THAN:
                    results.add(manager.getCorruptionLevel() > value);
                    break;
            }
        }

        return !results.contains(false);
    }

    @Override
    public ITextComponent getTooltip() {
        TranslationTextComponent text = new TranslationTextComponent("condition." + resource.getPath());
        text.appendString(": ");

        for (int i = 0; i < operations.size(); i++) {
            if (i > 0) text.appendString("& ");
            text.appendString(operations.get(i).toString() + " ");
        }

        text.appendString(String.valueOf(value));

        return text;
    }

    @Override
    public ResourceLocation getKey() {
        return resource;
    }

    public static IMutationCondition.IBuilder builder (int _value, Operation... _operations) {
        return () -> new CorruptionLevelCondition(_value, _operations);
    }

    public static class Serializer implements IConditionSerializer{

        @Override
        public IMutationCondition deserialize(JsonObject json) {
            List<Operation> operations = new ArrayList<>();
            json.getAsJsonArray("operations").forEach(element -> operations.add(Operation.valueOf(element.getAsString())));
            return builder(json.get("value").getAsInt(), operations.toArray(new Operation[0])).build();
        }

        @Override
        public JsonObject serialize(IMutationCondition _condition) {
            JsonObject out = new JsonObject();
            CorruptionLevelCondition condition = (CorruptionLevelCondition) _condition;
            out.addProperty("key", condition.resource.toString());
            out.addProperty("value", condition.value);
            JsonArray array = new JsonArray();

            for (Operation op : condition.operations) {
                array.add(op.toString());
            }

            if (array.size() == 0) array.add(Operation.EQUAL_TO.toString());

            out.add("operations", array);

            return out;
        }
    }

    public enum Operation {
        LESS_THAN,
        EQUAL_TO,
        GREATER_THAN
    }
}