package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;

public class GoodLuckMutation extends EffectMutation {
	public GoodLuckMutation() {
		super(
                "good_luck",
				"a2361e8f-1be0-478f-9742-a873400e9b6d"
		);

		modifiers.put(Attributes.LUCK.getRegistryName(), new AttributeModifier(
				uuid,
				mutName,
				1.0D,
				AttributeModifier.Operation.ADDITION
		));
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.BAD_LUCK);
	}
}