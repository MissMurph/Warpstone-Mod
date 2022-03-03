package com.lenin.warpstonemod.common.data;

import com.lenin.warpstonemod.common.WarpTags;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.blocks.WarpBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {

	public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, WarpstoneMain.MOD_ID, existingFileHelper);
		registerTags();
	}

	@Override
	protected void registerTags() {
		getOrCreateBuilder(WarpTags.Blocks.ORES_WARPSTONE).add(WarpBlocks.WARPSTONE_ORE);
		getOrCreateBuilder(Tags.Blocks.ORES).addTag(WarpTags.Blocks.ORES_WARPSTONE);

		getOrCreateBuilder(WarpTags.Blocks.STORAGE_BLOCKS_WARPSTONE).add(WarpBlocks.WARPSTONE_BLOCK);
		getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(WarpTags.Blocks.STORAGE_BLOCKS_WARPSTONE);
	}
}