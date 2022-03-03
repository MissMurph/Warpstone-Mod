package com.lenin.warpstonemod.client;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.blocks.WarpBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
		super(gen, WarpstoneMain.MOD_ID, existingFileHelper);
		registerStatesAndModels();
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(WarpBlocks.WARPSTONE_ORE);
		simpleBlock(WarpBlocks.WARPSTONE_BLOCK);
	}
}