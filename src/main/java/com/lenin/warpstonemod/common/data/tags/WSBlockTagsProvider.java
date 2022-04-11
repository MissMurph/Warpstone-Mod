package com.lenin.warpstonemod.common.data.tags;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.WSBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class WSBlockTagsProvider extends BlockTagsProvider {

    public WSBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Warpstone.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags (){
        //WARPSTONE ORE TAGS
        getOrCreateBuilder(WSTags.Blocks.ORES_WARPSTONE).add(WSBlocks.WARPSTONE_ORE);           //Adding block to tag
        getOrCreateBuilder(Tags.Blocks.ORES).addTag(WSTags.Blocks.ORES_WARPSTONE);                //This is adding the tag to the group

        //WARPSTONE BLOCK TAGS
        getOrCreateBuilder(WSTags.Blocks.STORAGE_BLOCKS_WARPSTONE).add(WSBlocks.WARPSTONE_BLOCK);           //Adding block to tag
        getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(WSTags.Blocks.STORAGE_BLOCKS_WARPSTONE);        //This is adding the tag to the group
    }
}