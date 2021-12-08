package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class StrengthMutation extends EffectMutation {
	public StrengthMutation(int _id) {
		super(_id,
				"strength",
				"0c0bbd77-a45b-4e92-8f95-ebdd8a565e02",
				Rarity.COMMON);
	}

	/**This mutation increases knockback with an extra strength of 0.4f <br>
	 * TODO: Research how Strength is used in knockback calculations to understand
	 * it mathematically
	 */

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onKnockBack);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	/**This code is ripped from {@link LivingEntity#attackEntityFrom(DamageSource, float)} lines 1097 to 1105 <br>
	 *
	 */

	public void onKnockBack (LivingDamageEvent event) {
		if (!(event.getSource().getTrueSource() instanceof PlayerEntity)
				|| !containsInstance((LivingEntity) event.getSource().getTrueSource())
				|| !getInstance((LivingEntity) event.getSource().getTrueSource()).isActive()
		) return;

		LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
		LivingEntity entity = event.getEntityLiving();

		//This is to determine the angle of the knockback, d1 & d0 reflect the x and z direction of the knockback

		double d1 = attacker.getPosX() - entity.getPosX();
		double d0;

		for(d0 = attacker.getPosZ() - entity.getPosZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
			d1 = (Math.random() - Math.random()) * 0.01D;
		}

		entity.applyKnockback(0.4F, d1, d0);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager);
	}
}