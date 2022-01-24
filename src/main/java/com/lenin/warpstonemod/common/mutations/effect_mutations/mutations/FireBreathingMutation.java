package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.*;

public class FireBreathingMutation extends EffectMutation implements IMutationTick {
	public FireBreathingMutation(int _id) {
		super(_id,
				"fire_breathing",
				"9970d2cf-e6ba-4025-acf6-fc23ca0c3668",
				Rarity.RARE);
	}

	private Map<UUID, Integer> counterMap = new HashMap<>();

	private static final int INTERVAL = 40;

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
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
				|| !instanceMap.get(player.getUniqueID()).isActive()
		) return;

		counterMap.put(player.getUniqueID(), counterMap.get(player.getUniqueID()) - 1);

		if (counterMap.get(player.getUniqueID()) <= 0) {
			canHeal = true;
			counterMap.put(player.getUniqueID(), INTERVAL);
		}
	}

	public void onLivingAttack (LivingAttackEvent event) {
		if (!fireSources.contains(event.getSource())
				|| !containsInstance(event.getEntityLiving())
				|| !instanceMap.get(event.getEntityLiving().getUniqueID()).isActive()
		) return;

		event.setCanceled(true);

		if (canHeal) event.getEntityLiving().heal(1f);
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		counterMap.put(entity.getUniqueID(), INTERVAL);
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		counterMap.remove(entity.getUniqueID());
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.EXPLOSIVE);
	}
}