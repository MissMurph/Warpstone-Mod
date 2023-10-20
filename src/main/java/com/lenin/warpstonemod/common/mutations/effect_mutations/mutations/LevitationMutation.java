package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterMutation;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;

public class LevitationMutation extends CounterMutation implements IMutationTick {
	public LevitationMutation(ResourceLocation _key) {
		super(_key,
                300
		);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !instanceMap.containsKey(player.getUUID())
		) return;

		if (decrement(counterMap, player.getUUID()) && Warpstone.getRandom().nextInt(100) >= 90) {
			int duration = 20 + Warpstone.getRandom().nextInt(100);
			player.addEffect(new EffectInstance(Effects.LEVITATION, duration));
		}
	}

	@Override
	public void clearMutation(PlayerManager manager) {
		super.clearMutation(manager);

		if (manager.getParentEntity().level.isClientSide()) return;

		manager.getParentEntity().removeEffect(Effects.LEVITATION);
	}
}