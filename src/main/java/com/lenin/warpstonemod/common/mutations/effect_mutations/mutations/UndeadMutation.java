package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;

public class UndeadMutation extends EffectMutation {
	public UndeadMutation(int _id) {
		super(_id,
				"undead",
				"36588dba-9d9e-45be-b572-c0c571370054",
				Rarity.RARE);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onPotionApplicable);
		bus.addListener(this::onHeal);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onPotionApplicable (PotionEvent.PotionApplicableEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !(event.getPotionEffect().getPotion() == Effects.POISON)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		event.setResult(Event.Result.DENY);
	}

	public void onHeal (LivingHealEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		float value = event.getAmount() * 0.75f;
		event.setAmount(value);
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		entity.getAttribute(Attributes.MAX_HEALTH).applyNonPersistentModifier(new AttributeModifier(
				uuid,
				mutName,
				1f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote) return;

		entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(uuid);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.BLOOD_SUCKING);
	}
}