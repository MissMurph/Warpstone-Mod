package com.lenin.warpstonemod.common.weighted_random;

import net.minecraft.nbt.INBT;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightEntry <T> {
    private final T parent;
    private final int baseWeight;

    private int modWeight;

    private final INBT value;

    public WeightEntry (T _parent, int _baseWeight, @Nullable INBT _value) {
        parent = _parent;
        baseWeight = _baseWeight;

        value = _value;
    }

    public WeightEntry (T _parent, int _baseWeight) {
        this(_parent, _baseWeight, null);
    }

    public T get () {
        return parent;
    }

    public boolean applyModifier (WeightModifier modifier) {
        switch (modifier.getOp()){
            case ADDITION:
                modWeight += modifier.getValue();
                return true;

            case MULTIPLY_BASE:
                modWeight += baseWeight * modifier.getValue();
                return true;

            case MULTIPLY_TOTAL:
                modWeight += (baseWeight + modWeight) * modifier.getValue();
                return true;
        }

        return false;
    }

    public int getCombinedWeight () {
        return baseWeight + modWeight;
    }

    public INBT getValue () {
        return value;
    }
}
