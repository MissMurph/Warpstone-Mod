package com.lenin.warpstonemod.data;

import com.lenin.warpstonemod.Main;
import com.lenin.warpstonemod.ModTags;
import com.lenin.warpstonemod.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {

	public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, Main.MOD_ID, existingFileHelper);
		registerTags();
	}

	@Override
	protected void registerTags() {
		getOrCreateBuilder(ModTags.Blocks.ORES_WARPSTONE).add(Blocks.WARPSTONE_ORE.get());
		getOrCreateBuilder(Tags.Blocks.ORES).addTag(ModTags.Blocks.ORES_WARPSTONE);

		getOrCreateBuilder(ModTags.Blocks.STORAGE_BLOCKS_WARPSTONE).add(Blocks.WARPSTONE_BLOCK.get());
		getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.STORAGE_BLOCKS_WARPSTONE);
	}
}