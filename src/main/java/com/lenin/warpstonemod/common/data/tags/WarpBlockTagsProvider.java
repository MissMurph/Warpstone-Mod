package com.lenin.warpstonemod.common.data.tags;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.blocks.WarpBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class WarpBlockTagsProvider extends BlockTagsProvider {

    public WarpBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, WarpstoneMain.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags (){
        //WARPSTONE ORE TAGS
        getOrCreateBuilder(WarpTags.Blocks.ORES_WARPSTONE).add(WarpBlocks.WARPSTONE_ORE.get());     //Adding block to tag
        getOrCreateBuilder(Tags.Blocks.ORES).addTag(WarpTags.Blocks.ORES_WARPSTONE);                //This is adding the tag to the group

        //WARPSTONE BLOCK TAGS
        getOrCreateBuilder(WarpTags.Blocks.STORAGE_BLOCKS_WARPSTONE).add(WarpBlocks.WARPSTONE_BLOCK.get());     //Adding block to tag
        getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(WarpTags.Blocks.STORAGE_BLOCKS_WARPSTONE);        //This is adding the tag to the group
    }
}