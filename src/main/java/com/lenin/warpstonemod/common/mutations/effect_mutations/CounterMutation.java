package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.MutationInstance;
import com.lenin.warpstonemod.common.PlayerManager;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CounterMutation extends Mutation {
    public CounterMutation(ResourceLocation _key, int _interval) {
        super(_key);
        INTERVAL = _interval;
    }

    protected Map<UUID, Integer> counterMap = new HashMap<>();

    protected final int INTERVAL;

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
    public MutationInstance constructInstance(PlayerManager manager) {
        MutationInstance instance = super.constructInstance(manager);

        if (manager.getParentEntity().world.isRemote) return instance;

        counterMap.put(manager.getUniqueId(), INTERVAL);

        return instance;
    }

    @Override
    public void clearMutation(PlayerManager manager) {
        super.clearMutation(manager);

        if (manager.getParentEntity().world.isRemote) return;

        counterMap.remove(manager.getUniqueId());
    }
}