package com.lenin.warpstonemod.common.weighted_random;

public class WeightModifier {
    private final float value;
    private final Operation operation;

    public WeightModifier (float _value, Operation _operation) {
        value = _value;
        operation = _operation;
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