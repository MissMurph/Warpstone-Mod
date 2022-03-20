package com.lenin.warpstonemod.client.data;

import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class WarpItemModelProvider extends ItemModelProvider {
	public WarpItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, WarpstoneMain.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels (){
		withExistingParent("warpstone_ore", modLoc("block/warpstone_ore"));
		withExistingParent("warpstone_block", modLoc("block/warpstone_block"));

		ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

		builder("warpstone_shard", itemGenerated);
		builder("warpstone_dust", itemGenerated);
		builder("mutation_reset", itemGenerated);
		builder("corrupted_tome", itemGenerated);
	}

	protected ItemModelBuilder builder(String name, ModelFile itemGenerated) {
		return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
	}
}