package com.lenin.warpstonemod.common.data.tags;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.items.WarpItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class WarpItemTagsProvider extends ItemTagsProvider {
    public WarpItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, WarpstoneMain.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags (){
        copy(WarpTags.Blocks.ORES_WARPSTONE, WarpTags.Items.ORES_WARPSTONE);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);

        copy(WarpTags.Blocks.STORAGE_BLOCKS_WARPSTONE, WarpTags.Items.STORAGE_BLOCKS_WARPSTONE);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

        getOrCreateBuilder(WarpTags.Items.GEMS_WARPSTONE).add(WarpItems.WARPSTONE_SHARD);
        getOrCreateBuilder(Tags.Items.GEMS).addTag(WarpTags.Items.GEMS_WARPSTONE);

        getOrCreateBuilder(WarpTags.Items.DUSTS_WARPSTONE).add(WarpItems.WARPSTONE_DUST);
        getOrCreateBuilder(Tags.Items.DUSTS).addTag(WarpTags.Items.DUSTS_WARPSTONE);
    }
}