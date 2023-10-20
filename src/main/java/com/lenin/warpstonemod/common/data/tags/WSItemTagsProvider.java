package com.lenin.warpstonemod.common.data.tags;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.items.WSItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class WSItemTagsProvider extends ItemTagsProvider {
    public WSItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, Warpstone.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags (){
        copy(WSTags.Blocks.ORES_WARPSTONE, WSTags.Items.ORES_WARPSTONE);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);

        copy(WSTags.Blocks.STORAGE_BLOCKS_WARPSTONE, WSTags.Items.STORAGE_BLOCKS_WARPSTONE);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

        tag(WSTags.Items.GEMS_WARPSTONE).add(WSItems.WARPSTONE_SHARD);
        tag(Tags.Items.GEMS).addTag(WSTags.Items.GEMS_WARPSTONE);

        tag(WSTags.Items.DUSTS_WARPSTONE).add(WSItems.WARPSTONE_DUST);
        tag(Tags.Items.DUSTS).addTag(WSTags.Items.DUSTS_WARPSTONE);
    }
}