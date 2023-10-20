package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class CorrosiveTouchMutation extends Mutation {
	public CorrosiveTouchMutation(ResourceLocation _key) {
		super(_key);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamage);
	}

	public void onLivingDamage (LivingDamageEvent event) {
		if (!(event.getSource().getEntity() instanceof PlayerEntity)
				|| !instanceMap.containsKey(event.getSource().getEntity().getUUID())
				|| !Warpstone.getRandom().nextBoolean()
		) return;

		int ticks = Math.max(Math.round((float)Math.random() * 200f), 40);
		event.getEntityLiving().addEffect(new EffectInstance(Effects.POISON, ticks));
	}
}