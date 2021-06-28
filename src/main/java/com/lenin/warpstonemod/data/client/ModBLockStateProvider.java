package com.lenin.warpstonemod.data.client;

import com.lenin.warpstonemod.Main;
import com.lenin.warpstonemod.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBLockStateProvider extends BlockStateProvider {
	public ModBLockStateProvider (DataGenerator gen, ExistingFileHelper existingFileHelper) {
		super(gen, Main.MOD_ID, existingFileHelper);
		registerStatesAndModels();
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(Blocks.WARPSTONE_ORE.get());
		simpleBlock(Blocks.WARPSTONE_BLOCK.get());
	}
}