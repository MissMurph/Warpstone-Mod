package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.WarpFoods;
import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class WarpItems {
    public static final RegistryObject<Item> WARPSTONE_SHARD = Registration.ITEMS.register("warpstone_shard", ()
            -> new WarpstoneShard(new Item.Properties()
            .group(WarpstoneMain.MOD_GROUP)
            .food(WarpFoods.WARPSTONE_SHARD)));

    public static final RegistryObject<Item> WARPSTONE_DUST = Registration.ITEMS.register("warpstone_dust", ()
            -> new WarpstoneDust(new Item.Properties()
            .group(WarpstoneMain.MOD_GROUP)
            .food(WarpFoods.WARPSTONE_DUST)));

    public static final RegistryObject<Item> MUTATION_RESET = Registration.ITEMS.register("mutation_reset", () ->
            new MutationReset(new Item.Properties()
            .group(WarpstoneMain.MOD_GROUP)));

    public static void register () {}
}