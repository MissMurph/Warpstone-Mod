package com.lenin.warpstonemod.common.mutations.weights;

import net.minecraft.util.ResourceLocation;

public class MutateModifier {
    private final ResourceLocation target;
    private final float value;
    private final Operation operation;

    public MutateModifier (ResourceLocation _target, float _value, Operation _operation) {
        target = _target;
        value = _value;
        operation = _operation;
    }

    public ResourceLocation getTarget () {
        return target;
    }

    public float getValue () {
        return value;
    }

    public Operation getOp () {
        return operation;
    }

    public enum Operation {
        ADDITION,
        MULTIPLY_BASE,
        MULTIPLY_TOTAL
    }
}