package com.lenin.warpstonemod.data;

import com.lenin.warpstonemod.Item.Items;
import com.lenin.warpstonemod.Main;
import com.lenin.warpstonemod.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

	public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(dataGenerator, blockTagProvider, Main.MOD_ID, existingFileHelper);
		registerTags();
	}

	@Override
	protected void registerTags() {
		//copy copies the tag from the block tag to the item tag to save time
		copy(ModTags.Blocks.ORES_WARPSTONE, ModTags.Items.ORES_WARPSTONE);
		copy(Tags.Blocks.ORES, Tags.Items.ORES);

		copy(ModTags.Blocks.STORAGE_BLOCKS_WARPSTONE, ModTags.Items.STORAGE_BLOCKS_WARPSTONE);
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

		getOrCreateBuilder(ModTags.Items.GEMS_WARPSTONE).add(Items.WARPSTONE_SHARD.get());
		getOrCreateBuilder(Tags.Items.GEMS).addTag(ModTags.Items.GEMS_WARPSTONE);
	}
}
