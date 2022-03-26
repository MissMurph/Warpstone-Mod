package com.lenin.warpstonemod.common.data;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import net.minecraft.util.ResourceLocation;

public abstract class MutationCondition {
    protected final ResourceLocation resource;

    public MutationCondition (ResourceLocation _resource) {
        resource = _resource;
    }

    public abstract boolean act (JsonObject json, PlayerManager manager);

    protected ResourceLocation key (String key) {
        return new ResourceLocation(WarpstoneMain.MOD_ID, key);
    }
}