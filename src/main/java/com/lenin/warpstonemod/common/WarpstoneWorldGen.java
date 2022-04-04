package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.common.blocks.WarpBlocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class WarpstoneWorldGen {
    private static final ConfiguredFeature<?,?> WARPSTONE_GEN = registerOre(
            "warpstone_ore",
            Feature.ORE.withConfiguration(new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                            WarpBlocks.WARPSTONE_ORE.getDefaultState(),
                            4))
                    .range(64)
                    .count(16)
    );

    private static final ConfiguredFeature<?,?> WARPSTONE_GEN_DEEP = registerOre(
            "warpstone_ore",
            Feature.ORE.withConfiguration(new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                            WarpBlocks.WARPSTONE_ORE.getDefaultState(),
                            6))
                    .range(16)
                    .count(10)
    );

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> registerOre (String name, ConfiguredFeature<FC, ?> feature){
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Warpstone.MOD_ID + ":" + name, feature);
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, WARPSTONE_GEN);
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, WARPSTONE_GEN_DEEP);
    }

    public static void init(){}
}