package com.lenin.warpstonemod.common.events;

import com.lenin.warpstonemod.common.mutations.weights.MutateModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutateWeightCollectEvent extends PlayerEvent {
    private final Map<ResourceLocation, List<MutateModifier>> modifiers = new HashMap<>();

    public MutateWeightCollectEvent (PlayerEntity player) {
        super(player);
    }

    public void submitModifier (MutateModifier modifier) {
        modifiers.computeIfAbsent(modifier.getTarget(), res -> new ArrayList<>()).add(modifier);
    }

    public Map<ResourceLocation, List<MutateModifier>> getModifiers () {
        return modifiers;
    }
}