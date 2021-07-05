package com.lenin.warpstonemod.data.client;

import com.lenin.warpstonemod.WarpstoneMain;
import com.lenin.warpstonemod.blocks.WarpBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class WarpBlockStateProvider extends BlockStateProvider {

    public WarpBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, WarpstoneMain.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(WarpBlocks.WARPSTONE_ORE.get());
        simpleBlock(WarpBlocks.WARPSTONE_BLOCK.get());
    }
}