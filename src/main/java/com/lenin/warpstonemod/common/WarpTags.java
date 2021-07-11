package com.lenin.warpstonemod.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class WarpTags {

	public static final class Blocks {
		public static final ITag.INamedTag<Block> ORES_WARPSTONE = forgeTag("ores/warpstone");
		public static final ITag.INamedTag<Block> STORAGE_BLOCKS_WARPSTONE = forgeTag("storage_blocks/warpstone");

		private static ITag.INamedTag<Block> forgeTag(String path){
			return BlockTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
		}
	}

	public static final class Items {
		public static final ITag.INamedTag<Item> ORES_WARPSTONE = forgeTag("ores/warpstone");
		public static final ITag.INamedTag<Item> STORAGE_BLOCKS_WARPSTONE = forgeTag("storage_blocks/warpstone");

		public static final ITag.INamedTag<Item> GEMS_WARPSTONE = forgeTag("gems/warpstone");

		private static ITag.INamedTag<Item> forgeTag(String path){
			return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
		}
	}
}