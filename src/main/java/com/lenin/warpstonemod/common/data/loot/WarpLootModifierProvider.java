package com.lenin.warpstonemod.common.data.loot;

import com.lenin.warpstonemod.common.Warpstone;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class WarpLootModifierProvider extends GlobalLootModifierProvider {
	public WarpLootModifierProvider(DataGenerator gen) {
		super(gen, Warpstone.MOD_ID);
	}

	@Override
	protected void start() {
		add("loot_fortune", WarpLootModifierSerializers.LOOT_FORTUNE.get(), new LootModifierFortuneMutation(new ILootCondition[]{}));
		add("loot_blacklung", WarpLootModifierSerializers.LOOT_BLACKLUNG.get(), new LootModifierBlackLungMutation(new ILootCondition[]{}));
	}
}