package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.common.WarpFoods;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.Registration;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class WarpItems {
    public static final RegistryObject<Item> WARPSTONE_SHARD = Registration.ITEMS.register("warpstone_shard", ()
            -> new Item(new Item.Properties()
            .group(WarpstoneMain.MOD_GROUP)));

    public static final RegistryObject<Item> WARPSTONE_DUST = Registration.ITEMS.register("warpstone_dust", ()
            -> new WarpstoneDust(new Item.Properties()
            .group(WarpstoneMain.MOD_GROUP)
            .food(WarpFoods.WARPSTONE_DUST)));

    public static void register () {}
}