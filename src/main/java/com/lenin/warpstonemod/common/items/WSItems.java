package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.common.WSFoods;
import com.lenin.warpstonemod.common.Warpstone;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public class WSItems {
    public static final Item WARPSTONE_SHARD = register("warpstone_shard", ()
            -> new Item(new Item.Properties()
                .group(Warpstone.MOD_GROUP)));

    public static final Item WARPSTONE_DUST = register("warpstone_dust", MutateItem::new);

    public static final Item MUTATION_RESET = register("mutation_reset", () ->
            new MutationReset(new Item.Properties()
                .group(Warpstone.MOD_GROUP)));

    public static final Item CORRUPTED_TOME = register("corrupted_tome", () ->
            new CorruptedTomeItem(new Item.Properties()
                    .group(Warpstone.MOD_GROUP)));

    public static final Item HUMAN_MEAT = register("human_meat",
            () -> new Item(new Item.Properties()
                    .group(Warpstone.MOD_GROUP)
                    .food(WSFoods.HUMANT_MEAT)));

    private static Item register (String name, Supplier<Item> item) {
        Item i = item.get().setRegistryName(name);
        Warpstone.getProxy().getRegistration().register(i);
        return i;
    }

    public static void register () {}

    
}