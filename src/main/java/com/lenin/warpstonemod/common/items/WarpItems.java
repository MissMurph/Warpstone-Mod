package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.WarpFoods;
import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class WarpItems {
    public static final Item WARPSTONE_SHARD = register("warpstone_shard", ()
            -> new WarpstoneShard(new Item.Properties()
            .group(WarpstoneMain.MOD_GROUP)
            .food(WarpFoods.WARPSTONE_SHARD)));

    public static final Item WARPSTONE_DUST = register("warpstone_dust", ()
            -> new WarpstoneDust(new Item.Properties()
            .group(WarpstoneMain.MOD_GROUP)
            .food(WarpFoods.WARPSTONE_DUST)));

    public static final Item MUTATION_RESET = register("mutation_reset", () ->
            new MutationReset(new Item.Properties()
            .group(WarpstoneMain.MOD_GROUP)));

    private static Item register (String name, Supplier<Item> item) {
        Item i = item.get().setRegistryName(name);
        WarpstoneMain.getProxy().getRegistration().register(i);
        return i;
    }

    public static void register () {}
}