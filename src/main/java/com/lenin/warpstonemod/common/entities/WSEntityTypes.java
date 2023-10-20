package com.lenin.warpstonemod.common.entities;

import com.lenin.warpstonemod.common.Warpstone;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class WSEntityTypes {

    public static EntityType<RatEntity> RAT = register("rat",
            () -> EntityType.Builder.of(RatEntity::new, EntityClassification.CREATURE)
                    .sized(0.4f, 0.3f)
                    .build(Warpstone.key("rat").toString()));

    private static <T extends Entity> EntityType<T> register(String name, Supplier<EntityType<T>> builder)  {
        EntityType<T> entity = builder.get();
        entity.setRegistryName(name);
        Warpstone.getProxy().getRegistration().register(entity);
        return entity;
    }

    public static void register() {}
}
