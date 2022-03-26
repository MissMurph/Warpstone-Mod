package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ScalesMutation extends EffectMutation {
	public ScalesMutation() {
		super(
                "scales",
				"265aebfe-d019-4fed-b1a7-a3311ffc7562"
		);

		modifiers.put(Attributes.ARMOR.getRegistryName(), new AttributeModifier(
				uuid,
				mutName,
				0.5f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));

		modifiers.put(Attributes.ARMOR_TOUGHNESS.getRegistryName(), new AttributeModifier(
				uuid,
				mutName,
				0.5f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));

		modifiers.put(WSAttributes.HEALING.getKey(), new AttributeModifier(
				uuid,
				mutName,
				-0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	/**
	 * This mutation causes +50% armour & armour toughness, and -25% all healing
	 *
	 */

	@Override
	public void attachListeners(IEventBus bus) {
		//bus.addListener(this::onLivingHeal);
	}

	public void onLivingHeal (LivingHealEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		float amount = event.getAmount() - (event.getAmount() * 0.25f);
		event.setAmount(amount);
	}
}