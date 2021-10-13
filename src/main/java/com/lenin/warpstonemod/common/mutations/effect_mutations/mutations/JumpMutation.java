package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class JumpMutation extends EffectMutation implements IMutationTick {
	public JumpMutation(int _id) {
		super(_id,
				WarpMutations.nameConst + "effect.jump_boost",
				WarpMutations.nameConst + "effect.weak_legs",
				"jump_icon.png",
				"45c87f74-844f-410c-8de2-d9e8cf1cac2c");
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onDamage);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onDamage(LivingDamageEvent event){
		if (!(event.getEntityLiving() instanceof PlayerEntity)) return;

		instanceMap.forEach((uuid, mut) -> {
			if (event.getEntityLiving().getUniqueID() != mut.getParent().getUniqueID() || !mut.isActive() || mut.getMutationLevel() == 1) return;

			float damage = (event.getAmount() * 1.25f);
			if (damage < 2) damage = 2;

			if (event.getSource() == DamageSource.FALL) event.setAmount(damage);
		});
	}

	@Override
	public void mutationTick(PlayerEntity player) {
		instanceMap.forEach((uuid, mut) -> {
			if (!mut.isActive() || mut.getMutationLevel() == -1) return;

			if (mut.getParent().isPotionActive(Effects.JUMP_BOOST)) {
				for (EffectInstance e : mut.getParent().getActivePotionEffects()) {
					if (e.getDuration() < 1200) mut.getParent().addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 0, false, false));
				}
			}
			else {
				mut.getParent().addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 0, false, false));
			}
		});
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			entity.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 0, false, false));
		}
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			entity.removePotionEffect(Effects.JUMP_BOOST);
		}
	}
}