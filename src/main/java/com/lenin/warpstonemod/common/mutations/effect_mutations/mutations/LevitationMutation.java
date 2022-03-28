package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterEffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class LevitationMutation extends CounterEffectMutation implements IMutationTick {
	public LevitationMutation() {
		super("levitation",
                300
		);
	}

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

		if (decrement(counterMap, player.getUniqueID()) && WarpstoneMain.getRandom().nextInt(100) >= 90) {
			int duration = 20 + WarpstoneMain.getRandom().nextInt(100);
			player.addPotionEffect(new EffectInstance(Effects.LEVITATION, duration));
		}
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.getParentEntity().world.isRemote()) return;

		manager.getParentEntity().removePotionEffect(Effects.LEVITATION);
	}
}