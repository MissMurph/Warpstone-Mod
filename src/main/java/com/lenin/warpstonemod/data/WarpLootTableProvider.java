package com.lenin.warpstonemod.data;

import com.google.common.collect.ImmutableList;
import com.lenin.warpstonemod.Registration;
import com.lenin.warpstonemod.blocks.WarpBlocks;
import com.lenin.warpstonemod.items.WarpItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WarpLootTableProvider extends LootTableProvider {

    public WarpLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(WarpBlockLootTables::new, LootParameterSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationTracker) {
        map.forEach((p_218436_2_, p_218436_3_) -> LootTableManager.validateLootTable(validationTracker, p_218436_2_, p_218436_3_));
    }

    public static class WarpBlockLootTables extends BlockLootTables {

        @Override
        protected void addTables (){
            registerDropSelfLootTable(WarpBlocks.WARPSTONE_BLOCK.get());
            //registerLootTable(WarpBlocks.WARPSTONE_ORE.get(), droppingItemWithFortune(WarpBlocks.WARPSTONE_ORE.get(), WarpItems.WARPSTONE_SHARD.get()));

            registerLootTable(WarpBlocks.WARPSTONE_ORE.get(), (warpstoneOre) -> {
                return droppingWithSilkTouch(warpstoneOre,
                        withExplosionDecay(warpstoneOre, ItemLootEntry.builder(WarpItems.WARPSTONE_SHARD.get())
                                .acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 3.0F)))
                                .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))));
            });
        }

        @Override
        protected Iterable<Block> getKnownBlocks(){
            return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }
}