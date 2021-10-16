package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

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
		LivingEntity entity = event.getEntityLiving();
		if (entity.world.isRemote() || !(entity instanceof PlayerEntity) || !(instanceMap.containsKey(entity.getUniqueID()))) return;
		EffectMutationInstance instance = instanceMap.get(entity.getUniqueID());

		if (instance.isActive() && instance.getMutationLevel() == -1) {
			if (event.getSource() == DamageSource.FALL) {
				float damage = (event.getAmount() * 1.25f);
				if (damage < 2) damage = 2;

				if (event.getSource() == DamageSource.FALL) event.setAmount(damage);
			}
		}
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT) return;

		EffectMutationInstance instance = instanceMap.get(player.getUniqueID());
		if (instance == null || !instance.isActive()) return;

		if (instance.getMutationLevel() == 1) {
			if (player.isPotionActive(Effects.JUMP_BOOST)) {
				for (EffectInstance e : player.getActivePotionEffects()) {
					if (e.getPotion() == Effects.JUMP_BOOST && e.getDuration() < 1200) {
						player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 0, false, false));
					}
				}
			}
			else {
				player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 0, false, false));
			}
		}
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote()) return;

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			entity.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 0, false, false));
		}
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			entity.removePotionEffect(Effects.JUMP_BOOST);
		}
	}
}