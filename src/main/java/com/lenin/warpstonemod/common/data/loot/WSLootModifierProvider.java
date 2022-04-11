package com.lenin.warpstonemod.common.data.loot;

import com.lenin.warpstonemod.common.Warpstone;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class WSLootModifierProvider extends GlobalLootModifierProvider {
	public WSLootModifierProvider(DataGenerator gen) {
		super(gen, Warpstone.MOD_ID);
	}

	@Override
	protected void start() {
		add("loot_fortune", WSLootModifierSerializers.LOOT_FORTUNE.get(), new LootModifierFortuneMutation(new ILootCondition[]{}));
		add("loot_blacklung", WSLootModifierSerializers.LOOT_BLACKLUNG.get(), new LootModifierBlackLungMutation(new ILootCondition[]{}));
	}
}