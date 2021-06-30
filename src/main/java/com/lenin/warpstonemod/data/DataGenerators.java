package com.lenin.warpstonemod.data;

import com.lenin.warpstonemod.WarpstoneMain;
import com.lenin.warpstonemod.data.client.WarpBlockStateProvider;
import com.lenin.warpstonemod.data.client.WarpItemModelProvider;
import com.lenin.warpstonemod.data.tags.WarpBlockTagsProvider;
import com.lenin.warpstonemod.data.tags.WarpItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = WarpstoneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
	private DataGenerators() {}

	@SubscribeEvent
	public static void gatherData (GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

		gen.addProvider(new WarpBlockStateProvider(gen, fileHelper));
		gen.addProvider(new WarpItemModelProvider(gen, fileHelper));

		WarpBlockTagsProvider blockTags = new WarpBlockTagsProvider(gen, fileHelper);

		gen.addProvider(blockTags);
		gen.addProvider(new WarpItemTagsProvider(gen, blockTags, fileHelper));
	}
}