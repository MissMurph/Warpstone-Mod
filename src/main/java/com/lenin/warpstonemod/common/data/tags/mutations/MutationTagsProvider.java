package com.lenin.warpstonemod.common.data.tags.mutations;

import com.google.gson.*;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.data.WarpstoneDataProvider;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MutationTagsProvider extends WarpstoneDataProvider {

    public MutationTagsProvider (DataGenerator _generator) {
        super(_generator, "mutation_tags");
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        JsonArray array = new JsonArray();

        for (MutationTag tag : MutationTags.getEntries()) {
            array.add(tag.getResource().getPath());
            IDataProvider.save(GSON, cache, tag.serialize(), this.generator.getOutputFolder().resolve("data/warpstonemod/mutations/tags/" + tag.getResource().getPath() + ".json"));
        }
    }
}