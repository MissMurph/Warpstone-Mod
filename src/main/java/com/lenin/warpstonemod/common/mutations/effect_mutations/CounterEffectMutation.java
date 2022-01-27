package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CounterEffectMutation extends EffectMutation {
    public CounterEffectMutation(int _id, String _mutName, String _uuid, Rarity _rarity, int _interval) {
        super(_id, _mutName, _uuid, _rarity);
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

    protected boolean deincrement (Map<UUID, Integer> map, UUID uuid) {
        int value = map.get(uuid) - 1;
        map.put(uuid, value);

        if (value <= 0) {
            reset(map, uuid);
            return true;
        }
        else return false;
    }

    protected void reset (Map<UUID, Integer> map, UUID uuid) {
        if (map.get(uuid) != INTERVAL) map.put(uuid, INTERVAL);
    }

    protected void reset (Map<UUID, Integer> map, UUID uuid, int interval) {
        map.put(uuid, interval);
    }

    @Override
    public void applyMutation(LivingEntity entity) {
        super.applyMutation(entity);

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