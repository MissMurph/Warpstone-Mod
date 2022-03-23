package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class GlowingMutation extends EffectMutation implements IMutationTick {
	public GlowingMutation() {
		super(
                "glowing",
				"0d988324-bfef-4dd4-87a7-647364829c44"
		);
	}

	@Override
	public void attachListeners(IEventBus bus){

	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT ||
				!instanceMap.containsKey(player.getUniqueID()) ||
				!instanceMap.get(player.getUniqueID()).isActive()) return;

		if (!player.isGlowing()) player.setGlowing(true);
	}

	@Override
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.getParentEntity().world.isRemote()) return;

		manager.getParentEntity().setGlowing(true);
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.getParentEntity().world.isRemote()) return;

		manager.getParentEntity().setGlowing(false);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.INVISIBILITY);
	}
}