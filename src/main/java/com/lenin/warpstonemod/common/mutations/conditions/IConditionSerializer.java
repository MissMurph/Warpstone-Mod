package com.lenin.warpstonemod.common.mutations.conditions;

import com.google.gson.JsonObject;

import java.util.function.Supplier;

public interface IConditionSerializer {
    IMutationCondition deserialize (JsonObject json);
    JsonObject serialize (IMutationCondition condition);
}