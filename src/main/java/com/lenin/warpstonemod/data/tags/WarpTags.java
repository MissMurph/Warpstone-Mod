package com.lenin.warpstonemod.data.tags;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class WarpTags {
    public static final class Blocks {
        public static final ITag.INamedTag<Block> ORES_WARPSTONE = forge("ores/warpstone");
        public static final ITag.INamedTag<Block> STORAGE_BLOCKS_WARPSTONE = forge("storage_blocks/warpstone");

        private static ITag.INamedTag<Block> forge(String path){
            return BlockTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }
    }

    public static final class Items {
        public static final ITag.INamedTag<Item> ORES_WARPSTONE = forge("ores/warpstone");
        public static final ITag.INamedTag<Item> STORAGE_BLOCKS_WARPSTONE = forge("storage_blocks/warpstone");

        public static final ITag.INamedTag<Item> GEMS_WARPSTONE = forge("gems/warpstone");

        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }
    }
}