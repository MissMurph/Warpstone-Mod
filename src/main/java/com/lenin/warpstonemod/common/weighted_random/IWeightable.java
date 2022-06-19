package com.lenin.warpstonemod.common.weighted_random;

public interface IWeightable<T> {
    default WeightEntry<T> getWeight() {
        return new WeightEntry<>(get(), 100);
    }

    T get();
}