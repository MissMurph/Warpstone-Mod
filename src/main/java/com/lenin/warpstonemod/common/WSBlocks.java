package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.common.Warpstone;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

import java.util.function.Supplier;

public class WSBlocks {
	public static final Block WARPSTONE_ORE = registerBlock("warpstone_ore", () -> new Block(AbstractBlock.Properties
			.of(Material.STONE)
			.requiresCorrectToolForDrops()
			.harvestLevel(2)
			.harvestTool(ToolType.PICKAXE)
			.strength(3,3)
	));

	public static final Block WARPSTONE_BLOCK = registerBlock("warpstone_block", () -> new Block(AbstractBlock.Properties
			.of(Material.METAL)
			.requiresCorrectToolForDrops()
			.strength(5,6)
			.sound(SoundType.METAL)
	));

	private static Block blockRegistry(String name, Supplier<Block> block) {
		Block b = block.get().setRegistryName(name);
		return Warpstone.getProxy().getRegistration().register(b);
	}

	public static Block registerBlock(String name, Supplier<Block> block) {
		Block ref = blockRegistry(name, block);
		Warpstone.getProxy().getRegistration().register(new BlockItem(ref, new Item.Properties().tab(Warpstone.MOD_GROUP)).setRegistryName(name));
		return ref;
	}

	public static void register () {}
}