package com.lenin.warpstonemod.common.data;

import com.lenin.warpstonemod.common.blocks.WarpBlocks;
import com.lenin.warpstonemod.common.items.WarpItems;
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
        ShapelessRecipeBuilder.shapelessRecipe(WarpItems.WARPSTONE_DUST, 1)
                .addIngredient(WarpItems.WARPSTONE_SHARD, 3)
                .addCriterion("has_item", hasItem(WarpItems.WARPSTONE_SHARD))
                .build(consumer);

        //WARPSTONE BLOCK
        ShapelessRecipeBuilder.shapelessRecipe(WarpBlocks.WARPSTONE_BLOCK, 1)
                .addIngredient(WarpItems.WARPSTONE_SHARD, 9)
                .addCriterion("has_item", hasItem(WarpItems.WARPSTONE_SHARD))
                .build(consumer);

        //WARPSTONE SHARD
        ShapelessRecipeBuilder.shapelessRecipe(WarpItems.WARPSTONE_SHARD, 9)
                .addIngredient(WarpBlocks.WARPSTONE_BLOCK, 1)
                .addCriterion("has_item", hasItem(WarpBlocks.WARPSTONE_BLOCK))
                .build(consumer);
    }
}