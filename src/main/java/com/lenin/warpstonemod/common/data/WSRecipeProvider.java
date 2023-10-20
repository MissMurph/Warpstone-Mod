package com.lenin.warpstonemod.common.data;

import com.lenin.warpstonemod.common.WSBlocks;
import com.lenin.warpstonemod.common.items.WSItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;

import java.util.function.Consumer;

public class WSRecipeProvider extends RecipeProvider {
    public WSRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes (Consumer<IFinishedRecipe> consumer){
        //WARPSTONE DUST
        ShapelessRecipeBuilder.shapeless(WSItems.WARPSTONE_DUST, 1)
                .requires(WSItems.WARPSTONE_SHARD, 3)
                .unlockedBy("has_item", has(WSItems.WARPSTONE_SHARD))
                .save(consumer);

        //WARPSTONE BLOCK
        ShapelessRecipeBuilder.shapeless(WSBlocks.WARPSTONE_BLOCK, 1)
                .requires(WSItems.WARPSTONE_SHARD, 9)
                .unlockedBy("has_item", has(WSItems.WARPSTONE_SHARD))
                .save(consumer);

        //WARPSTONE SHARD
        ShapelessRecipeBuilder.shapeless(WSItems.WARPSTONE_SHARD, 9)
                .requires(WSBlocks.WARPSTONE_BLOCK, 1)
                .unlockedBy("has_item", has(WSBlocks.WARPSTONE_BLOCK))
                .save(consumer);
    }


}