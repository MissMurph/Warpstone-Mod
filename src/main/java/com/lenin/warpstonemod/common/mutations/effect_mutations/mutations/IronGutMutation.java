package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;
import java.util.stream.Collectors;

public class IronGutMutation extends EffectMutation {
	public IronGutMutation(ResourceLocation _key) {
		super(_key);
	}

	/**This mutation makes the player immune to the negative effects of any food item
	 */

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onItemUseFinish);
	}

	/**The method for this mutation is to check the consumed item for any potion Effects
	 * matching the type {@link EffectType#HARMFUL}. By calling {@link LivingEntityUseItemEvent.Finish}
	 * we're detecting the effects the tick they're applied, and then removing them
	 */

	public void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		List<Effect> effects = event.getItem().getItem().getFood().getEffects()
				.stream()
				.map(Pair::getFirst)
				.map(EffectInstance::getPotion)
				.filter(effect -> effect.getEffectType() == EffectType.HARMFUL)
				.collect(Collectors.toList());

		effects.forEach(effect -> {
			event.getEntityLiving().removePotionEffect(effect);
		});
	}
}