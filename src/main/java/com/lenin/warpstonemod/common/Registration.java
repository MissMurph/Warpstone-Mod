package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.common.blocks.WarpBlocks;
import com.lenin.warpstonemod.common.items.WarpItems;
import com.lenin.warpstonemod.common.mob_effects.WarpEffects;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Registration {
    public static final DeferredRegister<Item> ITEMS = create(ForgeRegistries.ITEMS);
    public static final DeferredRegister<Block> BLOCKS = create(ForgeRegistries.BLOCKS);
    public static final DeferredRegister<Effect> EFFECTS = create(ForgeRegistries.POTIONS);

    public static void init () {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        EFFECTS.register(modEventBus);

        WarpBlocks.register();
        WarpItems.register();
        WarpFoods.register();
        WarpEffects.init();
    }

    private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> create(IForgeRegistry<T> registryEntry){
        return DeferredRegister.create(registryEntry, WarpstoneMain.MOD_ID);
    }
}