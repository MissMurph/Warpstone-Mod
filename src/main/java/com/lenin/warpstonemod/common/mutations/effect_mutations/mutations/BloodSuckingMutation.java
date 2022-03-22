package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.attributes.WSAttributes;
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

		modifiers.put(Attributes.MAX_HEALTH.getAttributeName(), new AttributeModifier(
				uuid,
				mutName,
				-0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));

		modifiers.put(WSAttributes.HEALING.getAttributeName(), new AttributeModifier(
				uuid,
				mutName,
				-0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamage);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onLivingDamage (LivingDamageEvent event) {
		if (!(event.getSource().getTrueSource() instanceof PlayerEntity)
				|| event.getSource().isProjectile()
				|| !containsInstance(event.getSource().getTrueSource().getUniqueID())
				|| !getInstance(event.getSource().getTrueSource().getUniqueID()).isActive()
		) return;

		int amount = Math.round(event.getAmount() * 0.25f);
		((PlayerEntity) event.getSource().getTrueSource()).heal(amount);
	}

	@Override
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.getParentEntity().world.isRemote) return;

		manager.getAttribute(Attributes.MAX_HEALTH.getAttributeName()).applyModifier();
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.getParentEntity().world.isRemote) return;

		manager.getAttribute(Attributes.MAX_HEALTH.getAttributeName()).removeModifier(uuid);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.SCALES) && !manager.containsEffect(EffectMutations.UNDEAD);
	}
}