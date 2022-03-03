package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CounterEffectMutation extends EffectMutation {
    public CounterEffectMutation(String _mutName, String _uuid, Rarity _rarity, int _interval) {
        super(_mutName, _uuid, _rarity);
        INTERVAL = _interval;
    }

    protected Map<UUID, Integer> counterMap = new HashMap<>();

    protected final int INTERVAL;

    @Override
    public void attachListeners(IEventBus bus) {

    }

    @Override
    public void attachClientListeners(IEventBus bus) {

    }

    protected boolean decrement(Map<UUID, Integer> map, UUID playerUUID) {
        int value = map.get(playerUUID) - 1;
        map.put(playerUUID, value);

        if (value <= 0) {
            reset(map, playerUUID);
            return true;
        }
        else return false;
    }

    protected void reset (Map<UUID, Integer> map, UUID playerUUID) {
        if (map.get(playerUUID) != INTERVAL) map.put(playerUUID, INTERVAL);
    }

    //Use this when not using default interval
    protected void reset (Map<UUID, Integer> map, UUID playerUUID, int interval) {
        map.put(playerUUID, interval);
    }

    @Override
    public void constructInstance(LivingEntity entity) {
        super.constructInstance(entity);

        if (entity.world.isRemote) return;

        counterMap.put(entity.getUniqueID(), INTERVAL);
    }

    @Override
    public void deactivateMutation(LivingEntity entity) {
        super.deactivateMutation(entity);

        if (entity.world.isRemote) return;

        counterMap.remove(entity.getUniqueID());
    }
}