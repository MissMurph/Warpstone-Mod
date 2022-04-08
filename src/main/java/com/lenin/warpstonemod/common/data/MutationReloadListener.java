package com.lenin.warpstonemod.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.lenin.warpstonemod.common.mutations.effect_mutations.Mutations;
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
        objectIn.entrySet().stream()
                .filter(entry -> entry.getKey().getPath().contains("tags"))
                .map(Map.Entry::getValue)
                .forEach(json -> MutationTags.loadTagData(json.getAsJsonObject()));

        objectIn.entrySet().stream()
                .filter(entry -> entry.getKey().getPath().contains("trees"))
                .map(Map.Entry::getValue)
                .forEach(json -> Mutations.loadMutationTree(json.getAsJsonObject()));

        objectIn.entrySet().stream()
                .filter(entry -> !entry.getKey().getPath().contains("tags"))
                .filter(entry -> !entry.getKey().getPath().contains("trees"))
                .map(Map.Entry::getValue)
                .forEach(json -> Mutations.loadMutationData(json.getAsJsonObject()));
    }
}