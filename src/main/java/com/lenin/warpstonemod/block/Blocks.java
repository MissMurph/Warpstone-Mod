package com.lenin.warpstonemod.block;

import com.lenin.warpstonemod.Main;
import com.lenin.warpstonemod.Registration;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class Blocks {
	public static final RegistryObject<Block> SILVER_ORE = registerBlock("warpstone_ore", () -> new Block(AbstractBlock.Properties.create(Material.ANVIL)));

	private static <T extends Block> RegistryObject<T> blockRegistry(String name, Supplier<T> block) {
		return Registration.BLOCKS.register(name, block);
	}

	public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
		RegistryObject<T> ref = blockRegistry(name, block);
		Registration.ITEMS.register(name, () -> new BlockItem(ref.get(), new Item.Properties().group(Main.MOD_GROUP)));
		return ref;
	}

	public static void register () {}
}