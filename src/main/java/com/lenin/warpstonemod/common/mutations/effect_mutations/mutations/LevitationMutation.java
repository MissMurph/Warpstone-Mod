package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevitationMutation extends CounterEffectMutation implements IMutationTick {
	public LevitationMutation(int _id) {
		super(_id,
				"levitation",
				"45c87f74-844f-410c-8de2-d9e8cf1cac2c",
				Rarity.UNCOMMON,
				200);
	}

	private Map<UUID, Integer> counterMap = new HashMap<>();

	@Override
	public void attachListeners(IEventBus bus) {}

	@Override
	public void attachClientListeners(IEventBus bus) {}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !instanceMap.containsKey(player.getUniqueID())
				|| !instanceMap.get(player.getUniqueID()).isActive())
			return;

		if (deincrement(counterMap, player.getUniqueID()) && WarpstoneMain.getRandom().nextInt(100) >= 90) {
			int duration = 20 + WarpstoneMain.getRandom().nextInt(100);
			player.addPotionEffect(new EffectInstance(Effects.LEVITATION, duration));
		}
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		entity.removePotionEffect(Effects.LEVITATION);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.SLOW_FALLING);
	}
}