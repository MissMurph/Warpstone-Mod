package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ThornsMutation extends EffectMutation {
	public ThornsMutation(ResourceLocation _key) {
		super(_key);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamage);
	}

	public void onLivingDamage (LivingDamageEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| event.getSource().getEntity() == null
				|| !containsInstance(event.getEntityLiving())
		) return;

		float damage = event.getAmount() * 0.25f;
		event.getSource().getEntity().hurt(DamageSource.thorns(event.getEntityLiving()), damage);
	}
}