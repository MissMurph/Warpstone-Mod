package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class FrailBodyMutation extends Mutation {
	public FrailBodyMutation(ResourceLocation _key) {
		super(_key);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamge);
	}

	public void onLivingDamge (LivingDamageEvent event){
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !instanceMap.containsKey(event.getEntityLiving().getUniqueID())
				|| event.getSource().getTrueSource() == null
				|| !(Math.random() > 0.75f)
		) return;

		int ticks = Math.max(Math.round((float)Math.random() * 100), 20);
		event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.POISON, ticks));
	}
}