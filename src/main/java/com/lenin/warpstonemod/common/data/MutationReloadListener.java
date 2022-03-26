package com.lenin.warpstonemod.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class MutationReloadListener extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MutationReloadListener() {
        super(GSON, "mutations");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            if (entry.getKey().getPath().contains("tags")) {
                MutationTags.loadTagData(entry.getValue().getAsJsonObject());
                continue;
            }

            System.out.println(entry.getKey());
            if (Registration.EFFECT_MUTATIONS.containsKey(entry.getKey())) Registration.EFFECT_MUTATIONS.getValue(entry.getKey()).deserialize(entry.getValue().getAsJsonObject());
        }
    }
}