package com.lenin.warpstonemod.common.entities;

import com.lenin.warpstonemod.common.Warpstone;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public class WSEntityTypes {

    public static EntityType<RatEntity> RAT;

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder)  {
        EntityType<T> entity = builder.build(Warpstone.key(name).toString());
        entity.setRegistryName(name);
        Warpstone.getProxy().getRegistration().register(entity);
        return entity;
    }

    public static void register() {
        RAT = register("rat",
                EntityType.Builder.create(RatEntity.factory(), EntityClassification.CREATURE)
                        .setCustomClientFactory(((spawnEntity, world) -> new RatEntity(RAT, world)))
                        .size(0.4f, 0.3f));
    }
}
