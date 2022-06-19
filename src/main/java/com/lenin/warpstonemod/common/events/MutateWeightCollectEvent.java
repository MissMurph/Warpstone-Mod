package com.lenin.warpstonemod.common.events;

import com.lenin.warpstonemod.common.weighted_random.WeightModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutateWeightCollectEvent extends PlayerEvent {
    private final Map<ResourceLocation, List<WeightModifier>> modifiers = new HashMap<>();

    public MutateWeightCollectEvent (PlayerEntity player) {
        super(player);
    }

    public void submitModifier (ResourceLocation target, WeightModifier modifier) {
        modifiers.computeIfAbsent(target, res -> new ArrayList<>()).add(modifier);
    }

    public Map<ResourceLocation, List<WeightModifier>> getModifiers () {
        return modifiers;
    }
}