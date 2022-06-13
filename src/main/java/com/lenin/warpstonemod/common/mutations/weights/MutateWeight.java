package com.lenin.warpstonemod.common.mutations.weights;

import com.lenin.warpstonemod.common.mutations.Mutation;

public class MutateWeight {
    private final Mutation parent;
    private final int baseWeight;
    private int modWeight;

    public MutateWeight (Mutation _parent, int _baseWeight) {
        parent = _parent;
        baseWeight = _baseWeight;
    }

    public boolean applyModifier (MutateModifier modifier) {
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

    public Mutation getParent () {
        return parent;
    }
}