package com.lenin.warpstonemod.common.data;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.mutations.PlayerManager;

public interface IMutationCondition {
    boolean act(JsonObject json, PlayerManager manager);
    JsonObject serialize (Object... args);
}