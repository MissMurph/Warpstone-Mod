package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class WeakLiverMutation extends EffectMutation {
	public WeakLiverMutation(ResourceLocation _key) {
		super(_key);
	}

	/**This mutation inflicts Nausea on the player whenever they drink a potion
	 *
	 *
	 */

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onItemUseFinish);
	}

	public void onItemUseFinish (LivingEntityUseItemEvent.Finish event){
		if (event.getEntityLiving().level.isClientSide()
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| !(event.getItem().getItem() instanceof PotionItem)
				|| PotionUtils.getCustomEffects(event.getItem()).isEmpty()
				|| !containsInstance(event.getEntityLiving())
		) return;

		int ticks = (int) Math.max(200, Math.random() * 400);
		event.getEntityLiving().addEffect(new EffectInstance(Effects.CONFUSION, ticks));
	}

	@Override
	public void clearMutation(PlayerManager manager) {
		super.clearMutation(manager);

		if (manager.getParentEntity().level.isClientSide()) return;

		manager.getParentEntity().removeEffect(Effects.CONFUSION);
	}
}