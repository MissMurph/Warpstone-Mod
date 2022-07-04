package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class WeakLegsMutation extends Mutation {
	public WeakLegsMutation(ResourceLocation _key) {
		super(_key);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onDamage);
	}

	public void onDamage(LivingDamageEvent event){
		LivingEntity entity = event.getEntityLiving();
		if (entity.world.isRemote()
				|| !(entity instanceof PlayerEntity)
				|| !(instanceMap.containsKey(entity.getUniqueID()))) return;

		if (event.getSource() == DamageSource.FALL) {
			float damage = (event.getAmount() * 1.25f);
			if (damage < 2) damage = 2;

			if (event.getSource() == DamageSource.FALL) event.setAmount(damage);
		}
	}
}