package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;

public class UndeadMutation extends EffectMutation {
	public UndeadMutation(ResourceLocation _key) {
		super(_key);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onPotionApplicable);
	}

	public void onPotionApplicable (PotionEvent.PotionApplicableEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !(event.getPotionEffect().getPotion() == Effects.POISON)
				|| !containsInstance(event.getEntityLiving())
		) return;

		event.setResult(Event.Result.DENY);
	}
}