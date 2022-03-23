package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;

public class FinsMutation extends EffectMutation {
	public FinsMutation() {
		super(
                "fins",
				"26d0153a-08da-4c90-9287-44f1e6920e7d"
		);

		modifiers.put(net.minecraftforge.common.ForgeMod.SWIM_SPEED.getId(), new AttributeModifier(
				uuid,
				mutName,
				1f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.STRONG_LEGS);
	}
}