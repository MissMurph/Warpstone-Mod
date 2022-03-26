package com.lenin.warpstonemod.common.data.mutation_conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.data.IMutationCondition;
import com.lenin.warpstonemod.common.data.MutationCondition;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorruptionLevelCondition implements IMutationCondition {

    private final ResourceLocation resource;

    public CorruptionLevelCondition () {
        resource = new ResourceLocation(WarpstoneMain.MOD_ID, "corruption_level");
    }

    @Override
    public boolean act(JsonObject json, PlayerManager manager) {
        int reqValue = json.get("value").getAsInt();
        List<Boolean> operations = new ArrayList<>();

        for (JsonElement operation : json.getAsJsonArray("operations")) {
            Operation op = Operation.valueOf(operation.getAsString());

            switch (op) {
                case EQUAL_TO:
                    operations.add(manager.getCorruptionLevel() == reqValue);
                    break;
                case LESS_THAN:
                    operations.add(manager.getCorruptionLevel() < reqValue);
                    break;
                case GREATER_THAN:
                    operations.add(manager.getCorruptionLevel() > reqValue);
                    break;
            }
        }

        return !operations.contains(false);
    }

    @Override
    public JsonObject serialize(Object... args) {
        for () {

        }

        JsonObject out = new JsonObject();
        out.addProperty("key", this.resource.toString());
        out.addProperty("value", value);
        JsonArray array = new JsonArray();

        for (Operation op : operations) {
            array.add(op.toString());
        }

        if (array.size() == 0) array.add(Operation.EQUAL_TO.toString());

        out.add("operations", array);

        return out;
    }

    public enum Operation {
        LESS_THAN,
        EQUAL_TO,
        GREATER_THAN
    }
}