package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;

public class FortuneMutation extends EffectMutation {
	public FortuneMutation() {
		super(
                "fortune",
				"9b0a8faa-1888-409f-a2a4-b0aab39cc065"
		);
	}

	//This mutation is barren due to a need for a Global Loot Modifier to handle the logic
	//LootModifierFortuneMutation.java contains all the logic

	@Override
	public void attachListeners(IEventBus bus) {

	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}
}