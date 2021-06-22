package com.lenin.warpstonemod.Item;

import com.lenin.warpstonemod.Main;
import com.lenin.warpstonemod.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class Items {
    public static final RegistryObject<Item> WARPSTONE_SHARD = Registration.ITEMS.register("warpstone_shard", ()
            -> new Item(new Item.Properties().group(Main.MOD_GROUP)));

    public static void Register () {}
}