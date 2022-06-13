package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.common.items.WSItems;
import com.lenin.warpstonemod.common.mob_effects.WSEffects;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.Mutations;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import java.util.*;
import java.util.stream.Collectors;

public class Registration {
    public static List<Block> BLOCKS;
    public static IForgeRegistry<Mutation> MUTATIONS;

    private final Map<Class<?>, List<IForgeRegistryEntry<?>>> initialized = new HashMap<>();

    public Registration() {}

    public void attachListeners (IEventBus bus) {
        bus.addGenericListener(Item.class, this::registerItems);
        bus.addGenericListener(Block.class, this::registerBlocks);
        bus.addGenericListener(Mutation.class, this::registerMutations);
        bus.addGenericListener(Effect.class, this::registerEffects);
    }

    public static void onRegistryBuild(RegistryEvent.NewRegistry event) {
        MUTATIONS = new RegistryBuilder<Mutation>()
                .setName(new ResourceLocation(Warpstone.MOD_ID, "mutation"))
                .setType(Mutation.class)
                .create();
    }

    public <T extends IForgeRegistryEntry<T>> T register (T entry){
        Class<T> type = entry.getRegistryType();
        List<IForgeRegistryEntry<?>> list = initialized.computeIfAbsent(type, entries -> new LinkedList<>());
        list.add(entry);
        return entry;
    }

    private void registerItems (RegistryEvent.Register<Item> event) {
        WSItems.register();
        WSFoods.register();
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    private void registerBlocks (RegistryEvent.Register<Block> event) {
        WSBlocks.register();

        BLOCKS = initialized.get(event.getRegistry().getRegistrySuperType())
                .stream()
                .map(obj -> (Block) obj)
                .collect(Collectors.toList());

        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    private void registerEffects (RegistryEvent.Register<Effect> event) {
        WSEffects.register();
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    private void registerMutations (RegistryEvent.Register<Mutation> event) {
        Mutations.init();
        fillRegistry(event.getRegistry().getRegistrySuperType(), event.getRegistry());
    }

    private <T extends IForgeRegistryEntry<T>> void fillRegistry (Class<T> type, IForgeRegistry<T> registry) {
        initialized.get(type).forEach(entry -> registry.register((T) entry));
    }
}