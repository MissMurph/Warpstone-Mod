package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationTickHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FallingMutation extends EffectMutation implements IMutationTick {
	public FallingMutation(int _id) {
		super(_id,
				"effect.slow_falling",
				"effect.levitation",
				"fall_icon.png",
				"4e80c5c4-07ef-4ddb-85f9-e1901ba17103");

		MutationTickHelper.addListener(this);
		currentTickCount = TICK_COUNT;
	}

	protected static final int TICK_COUNT = 100;
	protected int currentTickCount;

	@Override
	public void attachListeners(IEventBus bus) {

	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void onTick(TickEvent event) {
		currentTickCount--;

		instanceMap.forEach((uuid, mut) -> {
			if (!mut.isActive() || mut.getMutationLevel() == -1) return;
			//if (!mut.getParent().world.isRemote()) System.out.println("Server Side");
			//else System.out.println("Client Side");



			if (mut.getParent().isPotionActive(Effects.SLOW_FALLING)) {
				for (EffectInstance e : mut.getParent().getActivePotionEffects()) {
					if (e.getDuration() < 1200) mut.getParent().addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 3600, 0, false, false));
				}
			}
			else {
				mut.getParent().addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 3600, 0, false, false));
			}
		});

		if (currentTickCount <= 0) {
			instanceMap.forEach((uuid, mut) -> {
				if (!mut.isActive()) return;

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
			entity.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 3600, 0, false, false));
		}
	}

	@Override
	public void clearMutation(LivingEntity entity) {
		super.clearMutation(entity);

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			entity.removePotionEffect(Effects.SLOW_FALLING);
		}
	}
}