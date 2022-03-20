package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class SlowFallMutation extends EffectMutation implements IMutationTick {
	public SlowFallMutation() {
		super(
                "slow_falling",
				"4e80c5c4-07ef-4ddb-85f9-e1901ba17103"
		);
	}

	@Override
	public void attachListeners(IEventBus bus) {}

	@Override
	public void attachClientListeners(IEventBus bus) {}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (!instanceMap.containsKey(player.getUniqueID())
				|| !instanceMap.get(player.getUniqueID()).isActive()) return;

		if (player.isPotionActive(Effects.SLOW_FALLING)) {
			for (EffectInstance e : player.getActivePotionEffects()) {
				if (e.getPotion() == Effects.SLOW_FALLING && e.getDuration() < 1200) {
					player.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 3600, 0, false, false));
				}
			}
		}
		else {
			player.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 3600, 0, false, false));
		}
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote()) return;

		entity.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 3600, 0, false, false));
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		entity.removePotionEffect(Effects.SLOW_FALLING);
	}
}