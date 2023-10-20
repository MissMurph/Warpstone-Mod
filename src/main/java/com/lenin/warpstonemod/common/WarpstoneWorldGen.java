package com.lenin.warpstonemod.common;

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
            Feature.ORE.configured(new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            WSBlocks.WARPSTONE_ORE.defaultBlockState(),
                            4))
                    .range(64)
                    .count(16)
    );

    private static final ConfiguredFeature<?,?> WARPSTONE_GEN_DEEP = registerOre(
            "warpstone_ore",
            Feature.ORE.configured(new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                            WSBlocks.WARPSTONE_ORE.defaultBlockState(),
                            6))
                    .range(16)
                    .count(10)
    );

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> registerOre (String name, ConfiguredFeature<FC, ?> feature){
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Warpstone.MOD_ID + ":" + name, feature);
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, WARPSTONE_GEN);
        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, WARPSTONE_GEN_DEEP);
    }

    public static void init(){}
}