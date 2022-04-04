package com.lenin.warpstonemod.common.data;

import com.lenin.warpstonemod.client.data.WarpBlockStateProvider;
import com.lenin.warpstonemod.client.data.WarpItemModelProvider;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.data.loot.WarpLootModifierProvider;
import com.lenin.warpstonemod.common.data.loot.WarpLootTableProvider;
import com.lenin.warpstonemod.common.data.tags.WarpBlockTagsProvider;
import com.lenin.warpstonemod.common.data.tags.WarpItemTagsProvider;
import com.lenin.warpstonemod.common.data.mutations.MutationDataProvider;
import com.lenin.warpstonemod.common.data.tags.MutationTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Warpstone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
	private DataGenerators() {}

	@SubscribeEvent
	public static void gatherData (GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

			//Models
		gen.addProvider(new WarpBlockStateProvider(gen, fileHelper));
		gen.addProvider(new WarpItemModelProvider(gen, fileHelper));

			//Tags
		WarpBlockTagsProvider blockTags = new WarpBlockTagsProvider(gen, fileHelper);
		gen.addProvider(blockTags);
		gen.addProvider(new WarpItemTagsProvider(gen, blockTags, fileHelper));
		gen.addProvider(new MutationTagsProvider(gen));

			//Misc.
		gen.addProvider(new WarpLootTableProvider(gen));
		gen.addProvider(new WarpRecipeProvider(gen));
		gen.addProvider(new WarpLootModifierProvider(gen));
		gen.addProvider(new MutationDataProvider(gen));
	}
}