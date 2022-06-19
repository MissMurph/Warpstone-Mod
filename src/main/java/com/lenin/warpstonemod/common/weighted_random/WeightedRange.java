package com.lenin.warpstonemod.common.weighted_random;

import com.ibm.icu.impl.Pair;
import com.lenin.warpstonemod.common.Warpstone;

import java.util.ArrayList;
import java.util.List;

public class WeightedRange <T> {
    private final List<WeightEntry<T>> entries = new ArrayList<>();

    public WeightedRange () {}

    public WeightedRange (IWeightable<T>... startingRange) {
        for (IWeightable<T> object : startingRange) {
            entries.add(object.getWeight());
        }
    }

    public WeightEntry<T> entry (T parent, int weight) {
        WeightEntry<T> entry = new WeightEntry<>(parent, weight);
        return this.entry(entry);
    }

    public WeightEntry<T> entry (WeightEntry<T> entry) {
        entries.add(entry);
        return entry;
    }

    public WeightEntry<T> getResult () {
        int totalWeight = 0;

        for (WeightEntry<T> entry : entries) {
            totalWeight += entry.getCombinedWeight();
        }

        int result = Warpstone.getRandom().nextInt(totalWeight);

        for (int i = entries.size() - 1; i > 0; i--) {
            totalWeight -= entries.get(i).getCombinedWeight();

            if (result >= totalWeight) return entries.get(i);
        }

        return null;
    }

    public List<WeightEntry<T>> getEntries () {
        return entries;
    }
}