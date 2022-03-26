package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class BloodSuckingMutation extends EffectMutation {
	public BloodSuckingMutation() {
		super(
                "blood_sucking",
				"0c3e6ecf-34ef-4ad6-8440-d06573f15fd3"
		);

		modifiers.put(Attributes.MAX_HEALTH.getRegistryName(), new AttributeModifier(
				uuid,
				mutName,
				-0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));

		modifiers.put(WSAttributes.HEALING.getKey(), new AttributeModifier(
				uuid,
				mutName,
				0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}
}