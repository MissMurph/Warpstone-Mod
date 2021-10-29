package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class CorrosiveTouchMutation extends EffectMutation {
	public CorrosiveTouchMutation(int _id) {
		super(_id,
				"corrosive_touch",
				"corrosive_touch.png",
				"8ee3692f-f855-43e3-8a9f-dffb37381995",
				Rarity.RARE);
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
				|| !instanceMap.containsKey(event.getSource().getTrueSource().getUniqueID())
				|| !instanceMap.get(event.getSource().getTrueSource().getUniqueID()).isActive()
				|| !WarpstoneMain.getRandom().nextBoolean()
		) return;

		int ticks = Math.max(Math.round((float)Math.random() * 200f), 40);
		event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.POISON, ticks));
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return manager.getCorruptionLevel() >= 2 && !manager.containsEffect(EffectMutations.FRAIL_BODY);
	}
}