package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ScalesMutation extends EffectMutation {
	public ScalesMutation() {
		super(
                "scales",
				"265aebfe-d019-4fed-b1a7-a3311ffc7562"
		);
	}

	/**
	 * This mutation causes +50% armour & armour toughness, and -25% all healing
	 *
	 */

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingHeal);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onLivingHeal (LivingHealEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		float amount = event.getAmount() - (event.getAmount() * 0.25f);
		event.setAmount(amount);
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote) return;

		entity.getAttribute(Attributes.ARMOR).applyNonPersistentModifier(new AttributeModifier(
				uuid,
				mutName,
				0.5f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));

		entity.getAttribute(Attributes.ARMOR_TOUGHNESS).applyNonPersistentModifier(new AttributeModifier(
				uuid,
				mutName,
				0.5f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote) return;

		entity.getAttribute(Attributes.ARMOR).removeModifier(uuid);
		entity.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(uuid);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.THORNS);
	}
}