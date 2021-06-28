package com.lenin.warpstonemod.data.client;

import com.lenin.warpstonemod.Main;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
	public ModItemModelProvider (DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Main.MOD_ID, existingFileHelper);
		registerModels();
	}

	@Override
	protected void registerModels (){
		withExistingParent("warpstone_ore", modLoc("block/warpstone_ore"));
		withExistingParent("warpstone_block", modLoc("block/warpstone_block"));

		ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

		builder("warpstone_shard", itemGenerated);
	}

	protected ItemModelBuilder builder(String name, ModelFile itemGenerated) {
		return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
	}
}