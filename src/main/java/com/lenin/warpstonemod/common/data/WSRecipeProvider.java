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
    protected void registerRecipes (Consumer<IFinishedRecipe> consumer){
        //WARPSTONE DUST
        ShapelessRecipeBuilder.shapelessRecipe(WSItems.WARPSTONE_DUST, 1)
                .addIngredient(WSItems.WARPSTONE_SHARD, 3)
                .addCriterion("has_item", hasItem(WSItems.WARPSTONE_SHARD))
                .build(consumer);

        //WARPSTONE BLOCK
        ShapelessRecipeBuilder.shapelessRecipe(WSBlocks.WARPSTONE_BLOCK, 1)
                .addIngredient(WSItems.WARPSTONE_SHARD, 9)
                .addCriterion("has_item", hasItem(WSItems.WARPSTONE_SHARD))
                .build(consumer);

        //WARPSTONE SHARD
        ShapelessRecipeBuilder.shapelessRecipe(WSItems.WARPSTONE_SHARD, 9)
                .addIngredient(WSBlocks.WARPSTONE_BLOCK, 1)
                .addCriterion("has_item", hasItem(WSBlocks.WARPSTONE_BLOCK))
                .build(consumer);
    }
}