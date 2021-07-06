package com.lenin.warpstonemod.data;

import com.lenin.warpstonemod.WarpstoneMain;
import com.lenin.warpstonemod.WarpTags;
import com.lenin.warpstonemod.blocks.WarpBlocks;
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
		getOrCreateBuilder(WarpTags.Blocks.ORES_WARPSTONE).add(WarpBlocks.WARPSTONE_ORE.get());
		getOrCreateBuilder(Tags.Blocks.ORES).addTag(WarpTags.Blocks.ORES_WARPSTONE);

		getOrCreateBuilder(WarpTags.Blocks.STORAGE_BLOCKS_WARPSTONE).add(WarpBlocks.WARPSTONE_BLOCK.get());
		getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(WarpTags.Blocks.STORAGE_BLOCKS_WARPSTONE);
	}
}