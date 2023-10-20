package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class StrengthMutation extends Mutation {
	public StrengthMutation(ResourceLocation _key) {
		super(_key);
	}

	//This mutation increases knockback with an extra strength of 0.4f

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onKnockBack);
	}

	/**This code is ripped from {@link LivingEntity#attackEntityFrom(DamageSource, float)} lines 1097 to 1105 <br>
	 *
	 */

	public void onKnockBack (LivingDamageEvent event) {
		if (!(event.getSource().getEntity() instanceof PlayerEntity)
				|| !containsInstance((LivingEntity) event.getSource().getEntity())
		) return;

		LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
		LivingEntity entity = event.getEntityLiving();

		//This is to determine the angle of the knockback, d1 & d0 reflect the x and z direction of the knockback

		double d1 = attacker.getX() - entity.getX();
		double d0;

		for(d0 = attacker.getZ() - entity.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
			d1 = (Math.random() - Math.random()) * 0.01D;
		}

		entity.knockback(0.4F, d1, d0);
	}
}