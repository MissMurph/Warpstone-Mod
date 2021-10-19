package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class FallingMutation extends EffectMutation implements IMutationTick {
	public FallingMutation(int _id) {
		super(_id,
				WarpMutations.nameConst + "effect.slow_falling",
				WarpMutations.nameConst + "effect.levitation",
				"fall_icon.png",
				"4e80c5c4-07ef-4ddb-85f9-e1901ba17103",
				Rarity.COMMON);

		currentTickCount = TICK_COUNT;
		state = 0;
	}

	protected final int TICK_COUNT = 300;
	protected int currentTickCount;

	protected int state;

	@Override
	public void attachListeners(IEventBus bus) {}

	@Override
	public void attachClientListeners(IEventBus bus) {}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT || !instanceMap.containsKey(player.getUniqueID()) || !instanceMap.get(player.getUniqueID()).isActive()) return;

		currentTickCount--;

		EffectMutationInstance instance = instanceMap.get(player.getUniqueID());

		if (instance.getMutationLevel() == 1) {
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

		if (instance.getMutationLevel() == -1) {
			if (currentTickCount <= 0 && WarpstoneMain.getRandom().nextInt(100) >= 90) {
				int duration = 20 + WarpstoneMain.getRandom().nextInt(100);
				player.addPotionEffect(new EffectInstance(Effects.LEVITATION, duration));
			}
		}

		if (currentTickCount <= 0) currentTickCount = TICK_COUNT;
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote()) return;

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			EffectInstance inst = new EffectInstance(Effects.SLOW_FALLING, 3600, 0, false, false);
			entity.addPotionEffect(inst);
		}
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			entity.removePotionEffect(Effects.SLOW_FALLING);
		}
	}
}