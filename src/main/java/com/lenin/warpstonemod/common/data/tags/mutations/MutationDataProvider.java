package com.lenin.warpstonemod.common.data.tags.mutations;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.data.WarpstoneDataProvider;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import java.io.IOException;

public class MutationDataProvider extends WarpstoneDataProvider {
    public MutationDataProvider (DataGenerator _generator) {
        super(_generator, "mutations");
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        for (EffectMutation mut : Registration.EFFECT_MUTATIONS.getValues()) {
            IDataProvider.save(GSON, cache, mut.serialize(), this.generator.getOutputFolder().resolve("data/warpstonemod/mutations/" + mut.getKey() + ".json"));
        }
    }
}