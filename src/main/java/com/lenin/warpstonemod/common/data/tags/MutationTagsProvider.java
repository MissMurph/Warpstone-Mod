package com.lenin.warpstonemod.common.data.tags;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.data.WarpstoneDataProvider;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MutationTagsProvider extends WarpstoneDataProvider {
    private static final List<MutationTagData> data = new ArrayList<>();

    public MutationTagsProvider (DataGenerator _generator) {
        super(_generator, "mutation_tags");
        buildTagData();
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        for (MutationTagData tag : data) {
            IDataProvider.save(GSON, cache, tag.serialize(), this.generator.getOutputFolder().resolve("data/warpstonemod/mutations/tags/" + tag.getPath() + ".json"));
        }
    }

    private ResourceLocation key (String key) {
        return new ResourceLocation(Warpstone.MOD_ID, key);
    }

    private void buildTagData () {
        data.add(new MutationTagData.Builder(key("common"), 1)
                .addType(MutationTag.Type.RARITY)
                .addFormatting(TextFormatting.WHITE)
                .create()
        );

        data.add(new MutationTagData.Builder(key("uncommon"), 2)
                .addType(MutationTag.Type.RARITY)
                .addFormatting(TextFormatting.YELLOW)
                .create()
        );

        data.add(new MutationTagData.Builder(key("rare"), 3)
                .addType(MutationTag.Type.RARITY)
                .addFormatting(TextFormatting.AQUA)
                .create()
        );

        data.add(new MutationTagData.Builder(key("negative"), -10)
                .addFormatting(TextFormatting.RED)
                .create()
        );

        data.add(new MutationTagData.Builder(key("permanent"), 1)
                .create()
        );

        data.add(new MutationTagData.Builder(key("child"), 0)
                .create()
        );
    }
}