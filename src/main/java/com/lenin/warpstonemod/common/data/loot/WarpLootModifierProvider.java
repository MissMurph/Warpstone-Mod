package com.lenin.warpstonemod.common.data.loot;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class WarpLootModifierProvider extends GlobalLootModifierProvider {
	public WarpLootModifierProvider(DataGenerator gen) {
		super(gen, WarpstoneMain.MOD_ID);
	}

	@Override
	protected void start() {
		add("loot_fortune", WarpLootModifierSerializers.LOOT_FORTUNE.get(), new LootModifierFortuneMutation(
				new ILootCondition[]{

		}));
	}
}