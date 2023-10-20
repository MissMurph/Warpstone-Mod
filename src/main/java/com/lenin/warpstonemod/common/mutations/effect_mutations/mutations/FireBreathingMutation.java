package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterMutation;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FireBreathingMutation extends CounterMutation implements IMutationTick {
	public FireBreathingMutation(ResourceLocation _key) {
		super(_key,
                40
		);
	}

	private final Set<DamageSource> fireSources = new HashSet<>(Arrays.asList(
			DamageSource.ON_FIRE,
			DamageSource.IN_FIRE,
			DamageSource.HOT_FLOOR
	));

	private boolean canHeal;

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingAttack);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
		) return;

		if (decrement(counterMap, player.getUUID())) {
			canHeal = true;
			counterMap.put(player.getUUID(), INTERVAL);
		}
	}

	public void onLivingAttack (LivingAttackEvent event) {
		if (!fireSources.contains(event.getSource())
				|| !containsInstance(event.getEntityLiving())
		) return;

		event.setCanceled(true);

		if (canHeal) {
			event.getEntityLiving().heal(2f);
			canHeal = false;
		}
	}
}