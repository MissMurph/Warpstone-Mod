package com.lenin.warpstonemod.client.data;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.blocks.WarpBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class WarpBlockStateProvider extends BlockStateProvider {

    public WarpBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Warpstone.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(WarpBlocks.WARPSTONE_ORE);
        simpleBlock(WarpBlocks.WARPSTONE_BLOCK);
    }
}