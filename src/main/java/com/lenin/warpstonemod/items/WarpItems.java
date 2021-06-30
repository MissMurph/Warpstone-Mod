package com.lenin.warpstonemod.items;

import com.lenin.warpstonemod.WarpstoneMain;
import com.lenin.warpstonemod.Registration;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class WarpItems {
    public static final RegistryObject<Item> WARPSTONE_SHARD = Registration.ITEMS.register("warpstone_shard", ()
            -> new Item(new Item.Properties().group(WarpstoneMain.MOD_GROUP)));

    public static void Register () {}
}