package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.IEventBus;

public class BadLuckMutation extends EffectMutation {
	public BadLuckMutation() {
		super(
                "bad_luck",
				"0942e8e9-295a-430f-9988-5537e4010648"
		);

		modifiers.put(Attributes.LUCK.getRegistryName(), new AttributeModifier(
				uuid,
				"mutation.bad_luck",
				-1.0D,
				AttributeModifier.Operation.ADDITION
		));
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.GOOD_LUCK);
	}
}