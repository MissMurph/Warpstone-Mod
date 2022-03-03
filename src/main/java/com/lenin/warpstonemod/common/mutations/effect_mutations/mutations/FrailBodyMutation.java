package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class FrailBodyMutation extends EffectMutation {
	public FrailBodyMutation() {
		super(
                "frail_body",
				"b09d14ec-beda-4ea9-bf80-2055535c1b99",
				Rarity.COMMON);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamge);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onLivingDamge (LivingDamageEvent event){
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !instanceMap.containsKey(event.getEntityLiving().getUniqueID())
				|| !instanceMap.get(event.getEntityLiving().getUniqueID()).isActive()
				|| event.getSource().getTrueSource() == null
				|| !(Math.random() > 0.75f)
		) return;

		int ticks = Math.max(Math.round((float)Math.random() * 100), 20);
		event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.POISON, ticks));
	}

	@Override
	public IFormattableTextComponent getMutationName() {
		return super.getMutationName().mergeStyle(TextFormatting.RED);
	}
}