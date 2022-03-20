package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterEffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColdBloodMutation extends CounterEffectMutation implements IMutationTick {
    public ColdBloodMutation() {
        super(
                "cold_blood",
                "ecf187d9-7a7d-4732-9a20-f44bfe64a615",
                200
        );
    }

    private static final List<Biome> LEGAL_BIOMES = new ArrayList<>(Arrays.asList(
            ForgeRegistries.BIOMES.getValue(Biomes.BADLANDS.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.BADLANDS_PLATEAU.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.BEACH.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.DEEP_WARM_OCEAN.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.DESERT.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.DESERT_HILLS.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.DESERT_LAKES.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.MODIFIED_BADLANDS_PLATEAU.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.WOODED_BADLANDS_PLATEAU.getRegistryName())
    ));

    private static final List<Biome> ILLEGAL_BIOMES = new ArrayList<>(Arrays.asList(
            ForgeRegistries.BIOMES.getValue(Biomes.SNOWY_TAIGA.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.SNOWY_TUNDRA.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.COLD_OCEAN.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.DEEP_COLD_OCEAN.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.DEEP_FROZEN_OCEAN.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.SNOWY_TAIGA_MOUNTAINS.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.SNOWY_TAIGA_HILLS.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.SNOWY_MOUNTAINS.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.SNOWY_BEACH.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.ICE_SPIKES.getRegistryName()),
            ForgeRegistries.BIOMES.getValue(Biomes.FROZEN_RIVER.getRegistryName())
    ));

    @Override
    public void mutationTick(PlayerEntity player, LogicalSide side) {
        if (side == LogicalSide.CLIENT
                || !containsInstance(player)
                || !getInstance(player).isActive()
        ) return;

        if (LEGAL_BIOMES.contains(player.world.getBiome(player.getPosition()))) {
            if (decrement(counterMap, player.getUniqueID())) {
                player.addPotionEffect(new EffectInstance(
                        Effects.REGENERATION,
                        20
                ));
            }
        }
        else if (ILLEGAL_BIOMES.contains(player.world.getBiome(player.getPosition()))) {
            if (decrement(counterMap, player.getUniqueID())) {
                player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
            }
        }
        else {
            reset(counterMap, player.getUniqueID());
        }
    }

    @Override
    public void attachListeners(IEventBus bus) {
        super.attachListeners(bus);
    }

    @Override
    public void attachClientListeners(IEventBus bus) {
        super.attachClientListeners(bus);
    }

    @Override
    public boolean isLegalMutation(MutateManager manager) {
        return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.THICK_FUR);
    }
}