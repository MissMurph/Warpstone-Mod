package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class FallingMutation extends EffectMutation implements IMutationTick {
	public FallingMutation(int _id) {
		super(_id,
				"effect.slow_falling",
				"effect.levitation",
				"fall_icon.png",
				"4e80c5c4-07ef-4ddb-85f9-e1901ba17103");

		currentTickCount = TICK_COUNT;
		state = 0;
	}

	protected final int TICK_COUNT = 100;
	protected int currentTickCount;

	protected int state;

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::testShindig);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void testShindig (LivingDamageEvent event) {
		if (event.getSource() == DamageSource.FALL) event.setCanceled(true);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		currentTickCount--;

		if (side == LogicalSide.CLIENT) return;

		instanceMap.forEach((uuid, mut) -> {
			if (mut.getParent().world.isRemote()) return;
 			if (!mut.isActive() || mut.getMutationLevel() == -1) return;

			if (!mut.getParent().isPotionActive(Effects.SLOW_FALLING)) {
				//for (EffectInstance e : mut.getParent().getActivePotionEffects()) {
					//if (e.getDuration() < 1200) {
						EffectInstance inst = new EffectInstance(Effects.SLOW_FALLING, 3600, 0, false, false);
						inst.setPotionDurationMax(true);
						mut.getParent().addPotionEffect(inst);
					//}
				//}
			}
		});

		if (currentTickCount <= 0) {
			instanceMap.forEach((uuid, mut) -> {
				if (mut.getParent().world.isRemote() || !mut.isActive()) return;

				if (mut.getMutationLevel() == -1 && WarpstoneMain.getRandom().nextInt(100) >= 95) {
					int duration = 20 + WarpstoneMain.getRandom().nextInt(40);
					mut.getParent().addPotionEffect(new EffectInstance(Effects.LEVITATION, duration));
					System.out.println("Levitation Check Successful");
				}
				else if (mut.getMutationLevel() == -1) System.out.println("Levitation Check Failed");
			});

			currentTickCount = TICK_COUNT;
		}
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			EffectInstance inst = new EffectInstance(Effects.SLOW_FALLING, 3600, 0, false, false);
			//inst.setPotionDurationMax(true);
			entity.addPotionEffect(inst);
		}
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			entity.removePotionEffect(Effects.SLOW_FALLING);
		}
	}
}