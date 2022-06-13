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

    private void buildTagData () {
        data.add(new MutationTagData.Builder(Warpstone.key("common"), 1)
                .addType(MutationTag.Type.RARITY)
                .addFormatting(TextFormatting.WHITE)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("uncommon"), 2)
                .addType(MutationTag.Type.RARITY)
                .addFormatting(TextFormatting.YELLOW)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("rare"), 3)
                .addType(MutationTag.Type.RARITY)
                .addFormatting(TextFormatting.AQUA)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("negative"), -10)
                .addFormatting(TextFormatting.RED)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("permanent"), 1)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("child"), 0)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("evolving"), 0)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("generic.max_health"), 0)
                .addType(MutationTag.Type.ATTRIBUTE)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("generic.attack_damage"), 0)
                .addType(MutationTag.Type.ATTRIBUTE)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("generic.movement_speed"), 0)
                .addType(MutationTag.Type.ATTRIBUTE)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("generic.armor"), 0)
                .addType(MutationTag.Type.ATTRIBUTE)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("generic.armor_toughness"), 0)
                .addType(MutationTag.Type.ATTRIBUTE)
                .create()
        );

        data.add(new MutationTagData.Builder(Warpstone.key("generic.harvest_speed"), 0)
                .addType(MutationTag.Type.ATTRIBUTE)
                .create()
        );
    }
}