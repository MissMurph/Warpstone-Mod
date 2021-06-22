package com.lenin.warpstonemod.Item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class WarpstoneItemGroup extends ItemGroup {
    public WarpstoneItemGroup (String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return ItemGroup.MATERIALS.getIcon();
    }
}